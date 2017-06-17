package screens;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import misc.Player;
import fileHandling.GiveUpException;
import fileHandling.WriteReadFile;
import gameManagers.Game;
/**
 * A Main Window frame to contain all the panels
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class MainWindow extends JFrame implements ActionListener, MouseListener{

	private int _rows = 8,_cols = 8;
	private Player _player1, _player2;
	private Game _game;
	private boolean gameExists=false;
	private Settings _settings;
	private Menu _menu;
	/**
	 * Default Constructor. Creates a new main window frame that will contain all the panels of the game
	 */
	public MainWindow(){
		super("Reversi");
		ImageIcon icon = new ImageIcon("images/icon.png");
		setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_menu = new Menu(this);
		_player1 = new Player("player1", 1, 2, false);
		_player2 = new Player("player2", -1, 2, true);
		_settings = new Settings(this, _player1.getName(), _player2.getName(), _rows, _cols);
		getContentPane().add(_menu);
		setSize(1300,850);
		setResizable(false);  
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
	/**
	 * Changes the game settings when the user goes back to the menu from the settings screen
	 * @param rows - new number of rows
	 * @param cols - new number of columns
	 * @param player1Name - new name for player 1
	 * @param player2Name - new name for player 1
	 * @param isPlayer1CPU - new boolean to determine if player 1 is the computer
	 * @param isPlayer2CPU - new boolean to determine if player 2 is the computer
	 */
	private void newSettings(int rows, int cols, String player1Name, String player2Name, boolean isPlayer1CPU, boolean isPlayer2CPU){
		boolean sizeChanged = false, playerKindChanged = false;
		if (_rows != rows || _cols != cols){
			_rows = rows;
			_cols = cols;
			sizeChanged = true;
		}
		if (_player1.getIsCPU() != isPlayer1CPU || _player2.getIsCPU() != isPlayer2CPU){
			_player1.setIsCPU(isPlayer1CPU);
			_player2.setIsCPU(isPlayer2CPU);
			playerKindChanged = true;
		}
		_player1.setName(player1Name);
		_player2.setName(player2Name);
		if (sizeChanged || playerKindChanged){ // if one of these attributes were changed, the current game is deleted (warned in settings screen)
			_menu.unableResumeSave(); // disable buttons
			_game = null;
			gameExists = false;
		}
	}
	

	/**
	 * Listens to all buttons in the system (except for the board buttons)
	 */
	public void actionPerformed(ActionEvent e) {
		WriteReadFile writerReader = new WriteReadFile();
		JButton[] menuButtons = _menu.getButtons();
		JButton source = (JButton)e.getSource();
		if (source == menuButtons[0]){ // new game button
			if (gameExists){
				int choice = JOptionPane.showConfirmDialog(null, "WARNING: There is a game currently loaded. Are you sure you would like to delete it?", "Warning", JOptionPane.YES_NO_OPTION);
                if(choice == 0){
                	createNewGame();
                }
			}
			else{
				createNewGame();
			}
		}
		else if (source == menuButtons[1]){ // resume game button
			getContentPane().removeAll();
			_game.setPlayer1Name(_player1.getName());
			_game.setPlayer2Name(_player2.getName());
			_game.updateStatusBar();
			add(_game);
			revalidate();
			repaint();
		}
		if (source ==menuButtons[2]){ //save game button
			try {
				writerReader.saveCurrentGame(_game); // write to txt file
			} catch (GiveUpException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Game Saved Successfully");
		}
		else if (source ==menuButtons[3]){ // load game button
			String savedString = writerReader.loadGameToString(); // retrieve a string from the txt file
			loadGame(savedString);
		}
		else if (source == menuButtons[4]){ // settings button
			getContentPane().removeAll();
			add(_settings);
			revalidate();
			repaint();
		}
		else if (source == menuButtons[5]) // quit button
			System.exit(0);
		else if (_game !=null && source == _game.getBackButton()){ // back button (from game panel)
			if (_game.getGameEnded()){
				_game = null;
				gameExists = false;
				_menu.unableResumeSave();
				getContentPane().removeAll();
				add(_menu);
				revalidate();
				repaint();
			}
			else{
				getContentPane().removeAll();
				_menu.enableResumeSave();
				add(_menu);
				revalidate();
				repaint();
			}
		}
		else if (source == _settings.getOKButton()){ // ok button (from settings panel)
			Component[] comps = _settings.getComponents();
			String player1Name = ((JTextField)comps[3]).getText();
			String player2Name = ((JTextField)comps[5]).getText();
			Component[] sizeComps = ((JPanel)comps[1]).getComponents();
			int rows = (Integer)((JSpinner)sizeComps[0]).getValue();
			int cols = (Integer)((JSpinner)sizeComps[2]).getValue();
			boolean isPlayer1CPU = false, isPlayer2CPU = false;
			isPlayer1CPU = ((JRadioButton)comps[8]).isSelected();
			isPlayer2CPU = ((JRadioButton)comps[11]).isSelected();
			newSettings(rows,cols,player1Name,player2Name, isPlayer1CPU, isPlayer2CPU); // set the new settings
			JOptionPane.showMessageDialog(null, "Settings changed successfully.");
			getContentPane().removeAll();
			add(_menu);
			revalidate();
			repaint();
		}
	}
	/**
	 * Creates a new game
	 */
	private void createNewGame(){
		Player currPlayer;
		if (_player1.getIsCPU() && !_player2.getIsCPU()){
			currPlayer=_player2;
		}
		else if (_player2.getIsCPU() && !_player1.getIsCPU()){
			currPlayer=_player1;
		} // if there's one computer and one human, the human starts
		else{ 
			double random = Math.random(); // coin toss.
			if (random < 0.5)
				currPlayer = _player1;
			else
				currPlayer = _player2;
		}
		_game = new Game(_player1, _player2, currPlayer, _rows, _cols, null, this);
		gameExists = true;
		getContentPane().removeAll();
		add(_game);
		_game.updateStatusBar();
		_game.setVisible(true);
		revalidate();
		repaint();
		_game.start();
	}
	/**
	 * Deciphers a string of a saved game and creates a new game according to it's state
	 * @param savedString - the string read from the txt file
	 */
	private void loadGame(String savedString){
		Player currPlayer, player1, player2;
		String lines[] = savedString.split("\\r?\\n");
		String player1Str[] = lines[1].split(": ");
		String player2Str[] = lines[2].split(": ");
		player1 = new Player(player1Str[0], 1, Integer.parseInt(player1Str[1].split(" ")[0]), Boolean.parseBoolean(player1Str[2]));
		player2 = new Player(player2Str[0],-1, Integer.parseInt(player2Str[1].split(" ")[0]), Boolean.parseBoolean(player2Str[2]));
		String currTurn[] = lines[0].split(" ");
		if (currTurn[1].equals("1")){
			currPlayer = player1;
		}
		else{
			currPlayer = player2;
		}
		String rowsStr[] = lines[3].split(": ");
		int rows = Integer.parseInt(rowsStr[1]);
		String colsStr[] = lines[4].split(": ");
		int cols = Integer.parseInt(colsStr[1]);
		String[][] tempArray = new String[rows][cols];
		int[][] array = new int[rows][cols];
		for (int x=5, b=0; x<lines.length; x++,b++){
			tempArray[b] = lines[x].split(",");
		}
		for (int i=0; i<array.length; i++){
			for (int j=0; j<array[0].length; j++){
				array[i][j] = Integer.parseInt(tempArray[i][j]);
			}
		}
		_game = new Game(player1, player2, currPlayer, rows, cols, array, this);
		_settings = new Settings(this,player1.getName(), player2.getName(), rows, cols);
		gameExists = true;
		getContentPane().removeAll();
		add(_game);
		_game.setVisible(true);
		revalidate();
		repaint();
		_game.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Auto-generated method stub
		
	}

	@Override
	/**
	 * Change the button image when mouse enters (graphics)
	 */
	public void mouseEntered(MouseEvent e) {
		JButton[] menuButtons = _menu.getButtons();
		JButton source = (JButton)e.getSource();
		if (source == menuButtons[0]){
			menuButtons[0].setIcon(new ImageIcon(getClass().getResource("/newGameButtonHover.png")));
		}
		else if (source == menuButtons[1]){
			menuButtons[1].setIcon(new ImageIcon(getClass().getResource("/resumeButtonHover.png")));
		}
		else if (source == menuButtons[2]){
			menuButtons[2].setIcon(new ImageIcon(getClass().getResource("/saveButtonHover.png")));
		}
		else if (source == menuButtons[3]){
			menuButtons[3].setIcon(new ImageIcon(getClass().getResource("/loadButtonHover.png")));
		}
		else if (source == menuButtons[4]){
			menuButtons[4].setIcon(new ImageIcon(getClass().getResource("/settingsButtonHover.png")));
		}
		else if (source == menuButtons[5]){
			menuButtons[5].setIcon(new ImageIcon(getClass().getResource("/quitButtonHover.png")));
		}
		else if (_game !=null && source == _game.getBackButton()){
			_game.getBackButton().setIcon(new ImageIcon(getClass().getResource("/backButtonHover.png")));
		}
		else if (source == _settings.getOKButton()){
			_settings.getOKButton().setIcon(new ImageIcon(getClass().getResource("/okButtonHover.png")));
		}
		
	}

	/**
	 * Change the button image back when mouse leaves (graphics)
	 */
	public void mouseExited(MouseEvent e) {
		JButton[] menuButtons = _menu.getButtons();
		JButton source = (JButton)e.getSource();
		if (source == menuButtons[0]){
			menuButtons[0].setIcon(new ImageIcon(getClass().getResource("/newGameButton.png")));
		}
		else if (source == menuButtons[1]){
			menuButtons[1].setIcon(new ImageIcon(getClass().getResource("/resumeButton.png")));
		}
		else if (source == menuButtons[2]){
			menuButtons[2].setIcon(new ImageIcon(getClass().getResource("/saveButton.png")));
		}
		else if (source == menuButtons[3]){
			menuButtons[3].setIcon(new ImageIcon(getClass().getResource("/loadButton.png")));
		}
		else if (source == menuButtons[4]){
			menuButtons[4].setIcon(new ImageIcon(getClass().getResource("/settingsButton.png")));
		}
		else if (source == menuButtons[5]){
			menuButtons[5].setIcon(new ImageIcon(getClass().getResource("/quitButton.png")));
		}
		else if (_game !=null && source == _game.getBackButton()){
			_game.getBackButton().setIcon(new ImageIcon(getClass().getResource("/backButton.png")));
		}
		else if (source == _settings.getOKButton()){
			_settings.getOKButton().setIcon(new ImageIcon(getClass().getResource("/okButton.png")));
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
