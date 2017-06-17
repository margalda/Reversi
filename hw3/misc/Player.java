package misc;

/**
 * A Class representing a Reversi player
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class Player {
	private int _score;
	private String _name;
	private int _playerNumber;
	private boolean _cpuPlayer;
	/**
	 * Default Constructor.
	 * @param name - player's name
	 * @param number - player's number (1 or -1)
	 * @param score - current score. by default - 2. Can be different if a game is loaded
	 * @param cpuPlayer - a boolean that determines if the player is the CPU or a human - true for CPU
	 */
	public Player (String name, int number, int score, boolean cpuPlayer){
		_score = score;
		_name = name;
		_playerNumber = number;
		_cpuPlayer = cpuPlayer;
	}
	
	public int getScore(){
		return _score;
	}
	
	public void setScore(int score){
		_score = score;
	}
	
	public String getName(){
		return _name;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public boolean getIsCPU(){
		return _cpuPlayer;
	}
	
	public void setIsCPU(boolean isCPU){
		_cpuPlayer = isCPU;
	}
	
	public int getPlayerNumber(){
		return _playerNumber;
	}
	
	public String toString(){
		return _name+": "+_score + " Points";
	}
}
