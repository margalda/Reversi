package fileHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The logic needed to write to a file
 * @author Daniel Margalit, Saar Scheinkman
 *
 */
public abstract class WriteToFileBase {
	protected abstract File promptFile() throws AbortException;
	protected abstract void writeInner(FileWriter f) throws IOException;
	protected abstract void reportError(Exception e);
	protected abstract void reportGiveUp(Exception e);
	/**
	 * Uses FileWriter to write each line to the file
	 * @throws GiveUpException
	 */
	public void writeToFile() throws GiveUpException {
		while (true) {
			try {
				File tFile = promptFile();
				try {
					FileWriter f = new FileWriter(tFile);
					writeInner(f);
					f.close();
					return;
				} catch (IOException e) {
					reportError(e);
					continue;
				}
			} catch (AbortException e) {
				reportGiveUp(e);
				throw new GiveUpException();
			}
		}
	}
}
