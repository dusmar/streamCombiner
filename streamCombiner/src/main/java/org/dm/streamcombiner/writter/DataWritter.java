package org.dm.streamcombiner.writter;

import java.io.BufferedWriter;
import java.io.IOException;

import org.dm.streamcombiner.model.Data;

/**
 * Writes Data objects to a buffered character output stream
 *
 *
 *
 * @author Dusan Maruscak
 */

public abstract class DataWritter extends BufferedWriter {

	public DataWritter(BufferedWriter writter) {
		super(writter);
	}

	public abstract void writeData(Data data) throws IOException;

	/**
	 * @throws IOException 
	 * 
	 */
	public void closeDataWritter() throws IOException {
		flush();
	}

}
