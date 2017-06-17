package screens;
import java.awt.*;
import javax.swing.*;

/**
 * The main menu panel.
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class Menu extends JPanel{
	private JButton[] _buttons;
	/**
	 * Default Constructor.
	 * @param mainWindow - the main window containing the panel
	 */
	public Menu(MainWindow mainWindow){
		_buttons = new JButton[6];
		_buttons[0] = new JButton();
		_buttons[0].setIcon(new ImageIcon(getClass().getResource("/newGameButton.png")));
		_buttons[0].setBorderPainted(false);
		_buttons[0].setContentAreaFilled(false);
		_buttons[0].setFocusPainted(false); 
		_buttons[0].addActionListener(mainWindow);
		_buttons[0].addMouseListener(mainWindow);
		_buttons[0].setAlignmentX(CENTER_ALIGNMENT);
		
		_buttons[1] = new JButton();
		_buttons[1].setEnabled(false);
		_buttons[1].setIcon(new ImageIcon(getClass().getResource("/resumeButton.png")));
		_buttons[1].setBorderPainted(false);
		_buttons[1].setContentAreaFilled(false);
		_buttons[1].setFocusPainted(false); 
		_buttons[1].addActionListener(mainWindow);
		_buttons[1].addMouseListener(mainWindow);
		_buttons[1].setAlignmentX(CENTER_ALIGNMENT);
		
		_buttons[2] = new JButton();
		_buttons[2].addActionListener(mainWindow);
		_buttons[2].addMouseListener(mainWindow);
		_buttons[2].setEnabled(false);
		_buttons[2].setIcon(new ImageIcon(getClass().getResource("/saveButton.png")));
		_buttons[2].setBorderPainted(false);
		_buttons[2].setContentAreaFilled(false);
		_buttons[2].setFocusPainted(false);
		_buttons[2].setAlignmentX(CENTER_ALIGNMENT);
		
		_buttons[3] = new JButton();
		_buttons[3].addActionListener(mainWindow);
		_buttons[3].addMouseListener(mainWindow);
		_buttons[3].setIcon(new ImageIcon(getClass().getResource("/loadButton.png")));
		_buttons[3].setBorderPainted(false);
		_buttons[3].setContentAreaFilled(false);
		_buttons[3].setFocusPainted(false); 
		_buttons[3].setAlignmentX(CENTER_ALIGNMENT);
		
		_buttons[4] = new JButton();
		_buttons[4].addActionListener(mainWindow);
		_buttons[4].addMouseListener(mainWindow);
		_buttons[4].setIcon(new ImageIcon(getClass().getResource("/settingsButton.png")));
		_buttons[4].setBorderPainted(false);
		_buttons[4].setContentAreaFilled(false);
		_buttons[4].setFocusPainted(false); 
		_buttons[4].setAlignmentX(CENTER_ALIGNMENT);
		
		_buttons[5] =  new JButton();
		_buttons[5].addActionListener(mainWindow);
		_buttons[5].addMouseListener(mainWindow);
		_buttons[5].setIcon(new ImageIcon(getClass().getResource("/quitButton.png")));
		_buttons[5].setBorderPainted(false);
		_buttons[5].setContentAreaFilled(false);
		_buttons[5].setFocusPainted(false); 
		_buttons[5].setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel header = new JLabel();
		header.setIcon(new ImageIcon(getClass().getResource("/header.png")));
		header.setAlignmentX(CENTER_ALIGNMENT);
		
		add(header);
		add(_buttons[0]);
		add(_buttons[1]);
		add(_buttons[2]);
		add(_buttons[3]);
		add(_buttons[4]);
		add(_buttons[5]);
		
		setBackground(Color.DARK_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setVisible(true);
	}
	
	public JButton[] getButtons(){
		return _buttons;
	}
	/**
	 * Enables the resume and save button. Used when there is an existing current game.
	 */
	public void enableResumeSave(){
		_buttons[1].setEnabled(true);
		_buttons[2].setEnabled(true);
	}
	/**
	 * Disables the resume and save button.
	 */
	public void unableResumeSave(){
		_buttons[1].setEnabled(false);
		_buttons[2].setEnabled(false);
	}
	/**
	 * Paints the background
	 */
	 public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    // Draw the background image.
		    g.drawImage(new ImageIcon(getClass().getResource("/green-circles_1600x1067_35618.jpg")).getImage(), 0, 0, this);
	 }
}
