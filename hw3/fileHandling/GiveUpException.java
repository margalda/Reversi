package fileHandling;

/**
 * 
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class GiveUpException extends Exception {

	public GiveUpException(){
		super("Writer Stopped");
	}
}
