package gameManagers;
import java.awt.Color;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;
/**
 * A Class which is an action listener. Used to listen to each button of the board separately.
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class BoardActions implements ActionListener, MouseListener{
	private int _x,_y;
	private int [][] _array;
	private JButton[][] _buttons;
	private Game _game;
	
	public BoardActions(int x, int y, int[][] array, JButton[][] buttons, Game game){
		_x = x;
		_y = y;
		_buttons = buttons;
		_array = array;
		_game = game;
	}
	
	/**
	 * When a button is clicked, check if the spot is available, and play the move.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!_game.isThereATimerRunning())
		{
			if (_array[_x][_y] != 0)
				return;
			boolean played = false;
			LinkedList<int[][]> moves = _game.getCurrMoves();
			for (int[][] i : moves)
				if (i[0][0] == _x && i[0][1] == _y){
					_game.updateBoard(i, _x, _y);
					played = true;
				}
			if(played){
				_game.startTimer(); // start the flip animation
			}
		}
	}

	/**
	 * When the mouse enters a spot on the grid, paint the relevant spots to indicate which discs would be eaten if the user presses that spot.
	 */
	public void mouseEntered(MouseEvent arg0) {
		if (!_game.isThereATimerRunning()){
			if (_array[_x][_y] != 0)
				return;
			LinkedList<int[][]> moves = _game.getCurrMoves();
			for (int[][] i : moves)
				if (i[0][0] == _x && i[0][1] == _y){
					paintBoard(i);
				}
		}
	}
	/**
	 * Paints the discs to be eaten in gray and the current mouse spot in yellow.
	 * @param move
	 */
	private void paintBoard(int[][] move){
		_buttons[_x][_y].setBackground(Color.YELLOW);
		int j = _y+move[1][1];
		int i = _x+move[1][0];
		int whosTurn = _game.getCurrPlayer().getPlayerNumber();
		while(i>=0 && j>=0 && i < _array.length && j < _array[0].length && _array[i][j]*(whosTurn) < 0){
			_buttons[i][j].setBackground(Color.DARK_GRAY);;
			i+=move[1][0];
			j+=move[1][1];
		}
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		_game.setDefaultColor(_buttons);
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Auto-generated method stub
		
	}
}
