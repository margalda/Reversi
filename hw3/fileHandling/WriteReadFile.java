package fileHandling;
import gameManagers.Game;
import java.io.*;

/**
 * A Class that hold the read/write actions. to be used by the other packages
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class WriteReadFile {
	/**
	 * 
	 * @param game - the Game instance to be saved
	 * @throws GiveUpException
	 * Creates a txt file with all the relevant details for saving the current game.
	 */
	public void saveCurrentGame(Game game) throws GiveUpException {
		WriteToFileBase tWrite = new WriteToFileBase() {
			protected File promptFile() throws AbortException {
				BufferedReader tIn = new BufferedReader(new StringReader("savedGame.txt"));
				try {
					String tFilename = tIn.readLine();
					if (tFilename == null || tFilename.isEmpty())
						throw new AbortException();
					return new File(tFilename);
				} catch (IOException e) {
					throw new AbortException();
				}
			}

			protected void reportError(Exception e) {
				e.printStackTrace();
			}

			protected void reportGiveUp(Exception e) {
				e.printStackTrace();
			}

			protected void writeInner(FileWriter f) throws IOException {
				PrintWriter tOut = new PrintWriter(f);
				int[][] boardArray = game.getBoard().getArray();
				tOut.println("currTurn: "+game.getCurrPlayer().getPlayerNumber());
				tOut.println(game.getPlayer1().toString() + ": " + game.getPlayer1().getIsCPU());
				tOut.println(game.getPlayer2().toString() + ": " + game.getPlayer2().getIsCPU());
				tOut.println("rows: "+boardArray.length);
				tOut.println("cols: "+boardArray[0].length);
				for (int i=0; i<boardArray.length; i++){
					for (int j=0; j<boardArray[0].length; j++){
						if (j == boardArray[0].length - 1){
							tOut.print(boardArray[i][j]);
						}
						else{
						tOut.print(boardArray[i][j]+",");
						}
					}
					tOut.println("");
				}
			}
		};
		tWrite.writeToFile();
	}
	/**
	 * Reads the txt file.
	 * @return A string made from the content of the txt file
	 */
	public String loadGameToString()
	{
		   String content = null;
		   File file = new File("savedGame.txt");
		   try {
		       FileReader reader = new FileReader(file);
		       char[] chars = new char[(int) file.length()];
		       reader.read(chars);
		       content = new String(chars);
		       reader.close();
		   } catch (IOException e) {
		       e.printStackTrace();
		   }
		   return content;
	}
}
