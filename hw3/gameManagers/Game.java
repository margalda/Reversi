package gameManagers;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import screens.Board;
import screens.MainWindow;
import misc.Player;

/**
 * The main logic class of the game, calculates all moves each turn, changes turns and generally manages the game.
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class Game extends JPanel implements ActionListener{

	private Board _board;
	private Player _player1,_player2,_currPlayer;
	private JPanel _statusBar;
	private JButton _backButton;
	private LinkedList<int[][]> _currMoves; //the available moves of the current player
	private boolean _gameEnded;
	
	/**
	 * Properties used for animations
	 */
	private Timer _timer, _wait;
	private ImageIcon[] _im;
	private int _frame;
	private LinkedList<int[]> _changedDiscs;
	
	/**
	 * Default Constructor.
	 * @param player1 - Player 1's instance
	 * @param player2 - Player 2's instance
	 * @param currPlayer - a pointer to indicate which turn it is currently (the first turn)
	 * @param rows - the number of rows to build the board
	 * @param cols - the number of columns to build the board
	 * @param array - the array representing the disc locations (if it's a new game - it is empty [null])
	 * @param menu - a pointer to the main window containing the game panel
	 */
	public Game(Player player1, Player player2, Player currPlayer, int rows, int cols, int[][] array, MainWindow menu){
		super();
		setLayout(new BorderLayout());
		_backButton = new JButton();
		_backButton.addActionListener(menu);
		_backButton.addMouseListener(menu);
		_backButton.setBorderPainted(false);
		_backButton.setContentAreaFilled(false);
		_backButton.setFocusPainted(false); 
		_backButton.setIcon(new ImageIcon(getClass().getResource("/backButton.png")));
		_player1=player1;
		_player2=player2;
		_currPlayer=currPlayer;
		_gameEnded = false;
		_board = new Board(rows, cols,_currPlayer.getPlayerNumber(), this, array);
		_statusBar = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		_statusBar.add(_backButton);
		_backButton.setAlignmentX(LEFT_ALIGNMENT);
		JLabel player1Label = new JLabel(player1.toString());
		player1Label.setFont(new Font("Arial", Font.BOLD, 20));
		_statusBar.add(player1Label);
		JLabel turnLabel;
		if (_currPlayer.getPlayerNumber() == 1)
			turnLabel = new JLabel(new ImageIcon(getClass().getResource("/left.png")));
		else
			turnLabel = new JLabel(new ImageIcon(getClass().getResource("/right.png")));
		_statusBar.add(turnLabel);
		JLabel player2Label = new JLabel(player2.toString());
		player2Label.setFont(new Font("Arial", Font.BOLD, 20));
		if (_currPlayer==_player1){
			player1Label.setForeground(Color.ORANGE);
			player2Label.setForeground(Color.WHITE);
		}
		else{
			player2Label.setForeground(Color.ORANGE);
			player1Label.setForeground(Color.WHITE);
		}
		_statusBar.setOpaque(false);
		_statusBar.add(player2Label);
		add(_statusBar,BorderLayout.NORTH);
		add(_board, BorderLayout.CENTER);
		_currMoves = new LinkedList<int[][]>();
		setAllPossibleMoves();
		_timer = new Timer(30,this);
		_wait = new Timer(600,this);
		_im = new ImageIcon[11];
		_im[0] = new ImageIcon(getClass().getResource("/rsz_1.png"));
		_im[1] = new ImageIcon(getClass().getResource("/rsz_2.png"));
		_im[2] = new ImageIcon(getClass().getResource("/rsz_3.png"));
		_im[3] = new ImageIcon(getClass().getResource("/rsz_4.png"));
		_im[4] = new ImageIcon(getClass().getResource("/rsz_5.png"));
		_im[5] = new ImageIcon(getClass().getResource("/rsz_6.png"));
		_im[6] = new ImageIcon(getClass().getResource("/rsz_7.png"));
		_im[7] = new ImageIcon(getClass().getResource("/rsz_8.png"));
		_im[8] = new ImageIcon(getClass().getResource("/rsz_9.png"));
		_im[9] = new ImageIcon(getClass().getResource("/rsz_10.png"));
		_im[10] = new ImageIcon(getClass().getResource("/rsz_11.png"));
		_frame = 0;
		_changedDiscs = new LinkedList<int[]>();
	}
	/**
	 * used to start a game if the cpu should play first (happens on loading or if the game is CPU vs. CPU
	 */
	public void start(){
		if (_currPlayer.getIsCPU()){
			playCPU();
		}
	}
	/**
	 * Changes the turn.
	 */
	public void nextTurn(){
		if (_currPlayer == 	_player1){
			_currPlayer = _player2;
			_board.setWhosTurn(-1);
		}
		else{
			_currPlayer = _player1;
			_board.setWhosTurn(1);
		}
		if(checkForWinner()) // each round, check for a winner
			return;
		updateStatusBar();
		setAllPossibleMoves();
		if (_currPlayer.getIsCPU()){
			playCPU();
		}
	}
	/**
	 * Play a computer turn.
	 */
	private void playCPU(){
		LinkedList<int[][]> possibleMoves = getCurrMoves();
		if (possibleMoves.isEmpty()){
			JOptionPane.showMessageDialog(null, _currPlayer.getName() + " Has No Possible Moves. Current turn skipped.");
		}
		else{
			playBestMove(possibleMoves);
			startTimer(); // start the flip animation
		}
	}
	/**
	 * Play the best move currently possible using a certain strategy.
	 * @param possibleMoves - current list of moves available
	 */
	private void playBestMove(LinkedList<int[][]> possibleMoves){
		int rows = _board.getArray().length;
		int cols = _board.getArray()[0].length;
		boolean chosen = false;
		int chosenI=0, chosenJ=0;
		for (int[][] currentMove : possibleMoves){ //first, check if there is a corner move possible. If so, that is the best move - preform it.
			if (currentMove[0][0] == 0 && currentMove[0][1] == 0){ //top left corner
				chosenI = 0;
				chosenJ = 0;
				chosen = true;
				break;
			}
			else if (currentMove[0][0] == (rows - 1) && currentMove[0][1] == 0){ //bottom left corner
				chosenI = rows - 1;
				chosenJ = 0;
				chosen = true;
				break;
			}
			else if (currentMove[0][0] == 0 && currentMove[0][1] == (cols - 1)){ //top left corner
				chosenI = 0;
				chosenJ = cols - 1;
				chosen = true;
				break;
			}
			else if (currentMove[0][0] ==  (rows - 1) && currentMove[0][1] == (cols - 1)){//bottom right corner
				chosenI = rows - 1;
				chosenJ = cols - 1;
				chosen = true;
				break;
			}
		}
		if (!chosen){// if there's no available corner move, make closest move to board center
			LinkedList<Integer> distances = new LinkedList<Integer>();
			LinkedList<Integer> copy = new LinkedList<Integer>();
			for (int[][] move : possibleMoves){
				int tDistance = calculateDistance(move[0][0], move[0][1]);
				distances.add(tDistance);
				copy.add(tDistance);
			}
			copy.sort(null); // to get the minimum
			int tMinDistance = copy.get(0);
			int tMinIndex = distances.indexOf(tMinDistance);
			int[][] tChosenMove = possibleMoves.get(tMinIndex);
			chosenI = tChosenMove[0][0];
			chosenJ = tChosenMove[0][1];
		}
		for (int[][] i : possibleMoves){ // preform the chosen move
			if (i[0][0] == chosenI && i[0][1] == chosenJ){
				updateBoard(i, chosenI, chosenJ);
			}
		}
	}
	/**
	 * Updates the board after a move (changes the int's in the disc locations-array)
	 * @param move - current move to be made. represented in an 2x2 array: [i,j]
	 * 																	   [0/1/-1,0/1/-1] (this row represents the direction - there are 8 options in the square)
	 * @param x - the X coordinate of the spot clicked (or chosen by the CPU)
	 * @param y - the Y coordinate of the spot clicked (or chosen by the CPU)
	 */
	public void updateBoard(int[][] move, int x, int y){
		int[][] array = _board.getArray();
		int j = y+move[1][1];
		int i = x+move[1][0];
		ImageIcon currentIcon;
		int whosTurn = _currPlayer.getPlayerNumber();
		if (whosTurn == 1){
			currentIcon = _im[0]; // white disc
		}
		else{
			currentIcon = _im[10]; // black disc
		}
		while(i>=0 && j>=0 && i < array.length && j < array[0].length && array[i][j]*(whosTurn) < 0){ // while we're in the borders and until we encounter a friendly disc
			array[i][j] = whosTurn;
			_changedDiscs.add(new int[]{i,j}); // save all the disc locations that need to be flipped (for the animation)
			i+=move[1][0];
			j+=move[1][1];
		}
		array[x][y] = whosTurn; 
		_board.getButtons()[x][y].setIcon(currentIcon); // change the last disc (the chose spot)
	}
	/**
	 * finds all the current possible moves and sets a list of them.
	 */
	private void setAllPossibleMoves(){
		LinkedList<int[][]> tMoves = new LinkedList<int[][]>();
		int[][] array = _board.getArray();
		for (int i = 0; i<array.length; i++)
			for (int j = 0; j<array[0].length; j++){
				if (array[i][j] == 0){
					if (isValidMove(i,j,-1,-1)){ // 8 if's to check 8 possible directions
						tMoves.add(new int[][]{{i,j},{-1,-1}});
					}
					if (isValidMove(i,j,-1,0)){
						tMoves.add(new int[][]{{i,j},{-1,0}});
					}
					if (isValidMove(i,j,-1,1)){
						tMoves.add(new int[][]{{i,j},{-1,1}});
					}
					if (isValidMove(i,j,0,1)){
						tMoves.add(new int[][]{{i,j},{0,1}});
					}
					if (isValidMove(i,j,1,1)){
						tMoves.add(new int[][]{{i,j},{1,1}});
					}
					if (isValidMove(i,j,1,0)){
						tMoves.add(new int[][]{{i,j},{1,0}});
					}
					if (isValidMove(i,j,1,-1)){
						tMoves.add(new int[][]{{i,j},{1,-1}});
					}
					if (isValidMove(i,j,0,-1)){
						tMoves.add(new int[][]{{i,j},{0,-1}});
					}
				}
			}
		_currMoves = tMoves; // set the list
	}
	/**
	 * checks if a move is valid.
	 * @param x - chosen X coordinate.
	 * @param y - chosen X coordinate.
	 * @param addToRow - the number to add to the rows (the direction's first number)
	 * @param addToCol - the number to add to the columns (the direction's second number)
	 * @return true if move is valid, false otherwise
	 */
	private boolean isValidMove(int x, int y, int addToRow, int addToCol){
		int[][] array = _board.getArray();
		int whosTurn = _currPlayer.getPlayerNumber();
		boolean ans = true;
		int j = y+addToCol;
		int i = x+addToRow;
		if(i<0 || j<0 ||i >= array.length || j >= array[0].length || array[i][j]*(whosTurn) > 0) // if the addition causes the coordinates to "fall" from the grid, or if there is a friendly disc in the next spot
			ans = false;
		while(ans && i>=0 && j>=0 && i < array.length && j < array[0].length && array[i][j]*(whosTurn) < 0){
			i+=addToRow;
			j+=addToCol;
		}
		if(i<0 || j<0 || i >= array.length || j >= array[0].length || array[i][j]*(whosTurn) == 0)
			ans = false;
		
		return ans;
	}
	/**
	 * updates the status bar above the board after each turn
	 */
	public void updateStatusBar(){
		Component[] comps = _statusBar.getComponents();
		_player1.setScore(_board.countScore(_player1));
		_player2.setScore(_board.countScore(_player2));
		((JLabel)comps[1]).setText(_player1.toString());
		if (_currPlayer.getPlayerNumber() == 1){
			((JLabel)comps[2]).setIcon(new ImageIcon(getClass().getResource("/left.png")));
			((JLabel)comps[1]).setForeground(Color.ORANGE); // to notify who's turn
			((JLabel)comps[3]).setForeground(Color.WHITE);
		}
		else{
			((JLabel)comps[2]).setIcon(new ImageIcon(getClass().getResource("/right.png")));
			((JLabel)comps[3]).setForeground(Color.ORANGE);
			((JLabel)comps[1]).setForeground(Color.WHITE);
		}
		((JLabel)comps[3]).setText(_player2.toString());
	}
	
	public JButton getBackButton(){
		return _backButton;
	}
	/**
	 * Checks if there is a winner after each turn and delivers a relevant message.
	 * @return true if there is a winner, false otherwise.
	 */
	public boolean checkForWinner(){
		if (!_currMoves.isEmpty()){ //if there are possible moves, but one of the player has 0 discs
			if (_player1.getScore() == 0){ 
				JOptionPane.showMessageDialog(null, _player2.getName() + " won!");
				_gameEnded = true;
				_backButton.doClick();
			}
			else if (_player2.getScore() == 0){
				JOptionPane.showMessageDialog(null, _player1.getName() + " won!");
				_gameEnded = true;
				_backButton.doClick();
			}
		}
		else{ // if the game is "stuck" - no possible moves
			if (_player1.getScore() > _player2.getScore())
				JOptionPane.showMessageDialog(null, _player1.getName() + " won!");
			else if (_player2.getScore() > _player1.getScore())
				JOptionPane.showMessageDialog(null, _player2.getName() + " won!");
			else
				JOptionPane.showMessageDialog(null, "It's a Draw!");
			_gameEnded = true;
			_backButton.doClick(); // go back to main menu
		}
		return _gameEnded;
	}
	/**
	 * Draw the background image.
	 */
	public void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.drawImage(new ImageIcon(getClass().getResource("/woodPanel.png")).getImage(), 0, 0, this);
	 }

	public LinkedList<int[][]> getCurrMoves() {
		return _currMoves;
	}
	
	public Player getCurrPlayer(){
		return _currPlayer;
	}
	
	public Player getPlayer1(){
		return _player1;
	}
	
	public Player getPlayer2(){
		return _player2;
	}
	
	public Board getBoard(){
		return _board;
	}
	
	public boolean getGameEnded(){
		return _gameEnded;
	}
	
	public void setBoard(Board board){
		_board = board;
	}
	
	public LinkedList<int[]> getChangedDiscs() {
		return _changedDiscs;
	}

	/**
	 * Listens to timer ticks. Used for animation and waiting between turns.
	 */
	public void actionPerformed(ActionEvent e) {
		int whosTurn = _currPlayer.getPlayerNumber();
		JButton[][] buttons = _board.getButtons();
		if (e.getSource() == _timer && whosTurn == 1){
			if (_frame < _im.length){
				for (int[] i : _changedDiscs){
					buttons[i[0]][i[1]].setIcon(_im[10-_frame]);
					repaint();
				}
				_frame = _frame + 1;
			}
			else{
				_frame = 0;
				_timer.stop();
				_changedDiscs = new LinkedList<int[]>();
				_wait.start(); // when finished animating, clear the list, stop the "_timer" and start the "_wait" timer to create a hold between turns.
			}
		}
		
		else if (e.getSource() == _timer && whosTurn == -1){
			if (_frame < _im.length){
				for (int[] i : _changedDiscs){
					buttons[i[0]][i[1]].setIcon(_im[_frame]);
					repaint();
				}
				_frame = _frame + 1;
			}
			else{
				_frame = 0;
				_timer.stop();
				_changedDiscs = new LinkedList<int[]>();
				_wait.start();
			}
		}
		
		else if (e.getSource() == _wait){ // holds between 2 turns
			_wait.stop();
			setDefaultColor(_board.getButtons());
			nextTurn();
			if (getCurrMoves().isEmpty()){
				JOptionPane.showMessageDialog(null, "No Possible Moves. Current turn skipped.");
				nextTurn();
			}
		}
	}
	/**
	 * sets all the buttons to their original color - used after painting in yellow and gray
	 * @param buttons - the button array
	 */
	public void setDefaultColor(JButton[][] buttons){
		int[][] array = _board.getArray();
		for (int i = 0; i<array.length; i++)
			for (int j = 0; j<array[0].length; j++)
				buttons[i][j].setBackground(new Color(2263842));
	}
	/**
	 * Calculates the distance of a spot from the center of the grid in up-down and left-right steps (diagonals not counted)
	 * @param i
	 * @param j
	 * @return the distance from the center
	 */
	private int calculateDistance(int i, int j){ //gets i,j, calculates it's distance from the center
		int[] topleft = {_board.getArray().length/2 - 1, _board.getArray()[0].length/2 - 1};
		int[] bottomleft = {topleft[0] + 1, topleft[1]}; //
		int[] bottomright = {bottomleft[0], bottomleft[1] + 1};
		int ans = 1;
		
		while (i>bottomleft[0]){ // move in the described steps towards the closest center spot out of 4.
			i--;
			ans++;
		}
		while (j>bottomright[1]){
			j--;
			ans++;
		}
		while (i<topleft[0]){
			i++;
			ans++;
		}
		while(j<bottomleft[1]){
			j++;
			ans++;
		}
		return ans;
	}
	
	public void startTimer(){
		_timer.start();
	}
	
	public void setPlayer1Name(String name){
		_player1.setName(name);
	}
	
	public void setPlayer2Name(String name){
		_player2.setName(name);
	}

	public boolean isThereATimerRunning(){
		return _timer.isRunning() || _wait.isRunning();
	}
}

