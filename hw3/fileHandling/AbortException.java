package fileHandling;

/**
 * 
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public class AbortException extends Exception {
	
	public AbortException(){
		super("Writer Unexpectedlly aborted");
	}
}
