package screens;
import gameManagers.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import misc.Player;

/**
 * The Board panel. Has an array of int's and buttons (logic and graphic of the game)
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class Board extends JPanel implements ActionListener {
	
	private int [][] _array;
	private JButton [][] _buttons;
	private int _whosTurn;
	/**
	 * Default Constructor.
	 * @param rows - number of rows to create
	 * @param cols - number of columns to create
	 * @param whosTurn - a number representing the current turn
	 * @param game - a pointer to the current game panel that includes the board
	 * @param array - an array representing disc locations. used when loading, otherwise it is null
	 */
	public Board(int rows, int cols, int whosTurn, Game game, int[][] array){
		super(new GridLayout(rows,cols));
		JButton b;
		_array = new int[rows][cols];
		_buttons = new JButton[rows][cols];
		_whosTurn = whosTurn;
		for (int i = 0; i<rows; i++){
			for (int j = 0; j<cols; j++){
				b = new JButton();
				b.addActionListener(new BoardActions(i,j,_array, _buttons, game));
				b.addMouseListener(new BoardActions(i,j,_array, _buttons, game));
				b.addActionListener(this);
				b.setFocusPainted(false); 
				b.setBackground(new Color(2263842));
				add(b);
				_buttons[i][j] = b;
			}
		}
		if (array != null){ // means the game is being loaded
			_array=array;
			drawDiscs();
		}
		else{ // it is a new game
			placeFirstDiscs();
		}
		for (int i = 0; i<rows; i++){ // the listeners are added later so that the array is the right one
			for (int j = 0; j<cols; j++){
				_buttons[i][j].addActionListener(new BoardActions(i,j,_array, _buttons, game));
				_buttons[i][j].addMouseListener(new BoardActions(i,j,_array, _buttons, game));
				_buttons[i][j].addActionListener(this);
			}
		}
		
	}
	/**
	 * Draws a disc icon on each button according to the placed disc.
	 */
	private void drawDiscs(){
		ImageIcon whiteDisc = new ImageIcon(getClass().getResource("/rsz_1.png"));
		ImageIcon blackDisc = new ImageIcon(getClass().getResource("/rsz_11.png"));
		for (int i=0; i<_array.length; i++){
			for (int j=0; j<_array[0].length; j++){
				if (_array[i][j] == 1)
					_buttons[i][j].setIcon(whiteDisc);
				else if (_array[i][j] == -1)
					_buttons[i][j].setIcon(blackDisc);
			}
		}
	}
	/**
	 * places first 2 discs for each player in the board's center
	 * @param rows
	 * @param cols
	 */
	private void placeFirstDiscs(){
		int tCenterX = _array.length/2 - 1; 
		int tCenterY = _array[0].length/2 - 1; // rows and cols is an EVEN number.
		ImageIcon whiteDisc = new ImageIcon(getClass().getResource("/rsz_1.png"));
		ImageIcon blackDisc = new ImageIcon(getClass().getResource("/rsz_11.png"));
		
		_buttons[tCenterX][tCenterY].setIcon(whiteDisc);
		_array[tCenterX][tCenterY] = 1;

		_buttons[tCenterX+1][tCenterY+1].setIcon(whiteDisc);
		_array[tCenterX+1][tCenterY+1] = 1;
		
		// WHITES = Player 1, BLACKS = Player 2 (-1)
		
		_buttons[tCenterX+1][tCenterY].setIcon(blackDisc);
		_array[tCenterX+1][tCenterY] = -1;
		
		_buttons[tCenterX][tCenterY+1].setIcon(blackDisc);
		_array[tCenterX][tCenterY+1] = -1;
	}
	
	public int[][] getArray(){
		return _array;
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public int getWhosTurn(){
		return _whosTurn;
	}
	
	public void setWhosTurn(int whosTurn){
		_whosTurn = whosTurn;
	}
	/**
	 * counts the player's score
	 * @param player - the player to count his score
	 * @return	score
	 */
	public int countScore(Player player){
		int ans = 0;
		for (int i = 0; i<_array.length; i++)
			for (int j = 0; j<_array[0].length; j++){
				if (_array[i][j] == player.getPlayerNumber())
					ans++;
			}
		return ans;
	}
	
	public JButton[][] getButtons(){
		return _buttons;
	}
		
}
