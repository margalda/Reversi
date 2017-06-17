package screens;
import java.awt.*;
import javax.swing.*;
/**
 * The settings screen panel.
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class Settings extends JPanel{

	JButton _okButton;
	JPanel _panel;
	/**
	 * Default Constructor.
	 * @param menu - the main window containing the panel
	 * @param player1Name - current player 1's name
	 * @param player2Name - current player 2's name
	 * @param rows - number of rows of the board
	 * @param cols - number of columns of the board
	 */
	public Settings(MainWindow menu, String player1Name,String player2Name, int rows, int cols){
		super();
		Font defFont = new Font("Arial",Font.PLAIN, 18);
		Font titleFont = new Font("Arial", Font.BOLD, 20);
		_panel = new JPanel();
		_okButton = new JButton();
		_okButton.addActionListener(menu);
		_okButton.addMouseListener(menu);
		_okButton.setIcon(new ImageIcon(getClass().getResource("/okButton.png")));
		_okButton.setBorderPainted(false);
		_okButton.setContentAreaFilled(false);
		_okButton.setFocusPainted(false);
		_okButton.setAlignmentX(CENTER_ALIGNMENT);
		JLabel boardSize = new JLabel("Board Size:");
		boardSize.setFont(titleFont);
		boardSize.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(boardSize);
		JSpinner rowsSpinner = new JSpinner(new SpinnerNumberModel(rows,4,12,2));
		JSpinner colsSpinner =new JSpinner(new SpinnerNumberModel(rows,4,12,2));
		JFormattedTextField tf = ((JSpinner.DefaultEditor) rowsSpinner.getEditor()).getTextField();
		tf.setEditable(false);		
		tf = ((JSpinner.DefaultEditor) colsSpinner.getEditor()).getTextField();
		tf.setEditable(false);
		JLabel by = new JLabel("x");
		by.setFont(defFont);
		JPanel size = new JPanel();
		size.add(rowsSpinner);
		size.add(by);
		size.add(colsSpinner);
		size.setLayout(new FlowLayout(FlowLayout.CENTER));
		size.setOpaque(false);
		size.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(size);
		JLabel player1Title = new JLabel("Player 1's name:");
		player1Title.setFont(titleFont);
		player1Title.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player1Title);
		JTextField player1Text = new JTextField(player1Name);
		player1Text.setFont(defFont);
		player1Text.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player1Text);
		JLabel player2Title = new JLabel("Player 2's name:");
		player2Title.setFont(titleFont);
		player2Title.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player2Title);
		JTextField player2Text = new JTextField(player2Name);
		player2Text.setFont(defFont);
		player2Text.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player2Text);
		JLabel player1IsA = new JLabel("Player 1's kind:");
		player1IsA.setFont(titleFont);
		player1IsA.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player1IsA);
		JRadioButton player1Human = new JRadioButton("Human");
		JRadioButton player1Computer = new JRadioButton("CPU");
		ButtonGroup player1 = new ButtonGroup();
		player1Human.setOpaque(false);
		player1Computer.setOpaque(false);
		player1Human.setFont(defFont);
		player1Computer.setFont(defFont);
		player1Human.setSelected(true);
		player1Human.setAlignmentX(CENTER_ALIGNMENT);
		player1Computer.setAlignmentX(CENTER_ALIGNMENT);
		player1.add(player1Human);
		player1.add(player1Computer);
		_panel.add(player1Human);
		_panel.add(player1Computer);
		JLabel player2IsA = new JLabel("Player 2's kind:");
		player2IsA.setFont(titleFont);
		player2IsA.setAlignmentX(CENTER_ALIGNMENT);
		_panel.add(player2IsA);
		JRadioButton player2Human = new JRadioButton("Human");
		JRadioButton player2Computer = new JRadioButton("CPU");
		ButtonGroup player2 = new ButtonGroup();
		player2Human.setOpaque(false);
		player2Computer.setOpaque(false);
		player2Human.setFont(defFont);
		player2Computer.setFont(defFont);
		player2Computer.setSelected(true);
		player2Human.setAlignmentX(CENTER_ALIGNMENT);
		player2Computer.setAlignmentX(CENTER_ALIGNMENT);
		player2.add(player2Human);
		player2.add(player2Computer);
		_panel.add(player2Human);
		_panel.add(player2Computer);
		JLabel warningLabel = new JLabel("Warning: Changing game size or player kind will delete any unsaved games!");
		warningLabel.setFont(titleFont);
		warningLabel.setForeground(Color.RED);
		warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_panel.add(warningLabel);
		_panel.add(_okButton);
		_panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
		_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		_panel.setOpaque(false);
		add(_panel);
	}

	public JButton getOKButton(){
		return _okButton;
	}

	public Component[] getComponents(){
		return _panel.getComponents();
	}
	/**
	 * Paints the background
	 */
	 public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    // Draw the background image.
		    g.drawImage(new ImageIcon(getClass().getResource("/Light-Green-Circles.jpg")).getImage(), 0, 0, this);
	 }
}
