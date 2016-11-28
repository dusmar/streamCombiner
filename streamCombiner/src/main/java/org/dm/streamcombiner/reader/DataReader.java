package org.dm.streamcombiner.reader;

import java.io.BufferedReader;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Reads Data objects from a buffered character-input stream
 *
 *
 * @author Dusan Maruscak
 */

public abstract class DataReader extends BufferedReader {

	public DataReader(BufferedReader reader) {
		super(reader);
	}

	/**
	 * Reads Data object from stream.
	 * 
	 * @return Data Object or null if the end of the stream has been reached
	 * @throws ReadFromStreamException
	 *             If an I/O error occurs or Data object cannot be parsed
	 */
	public abstract Data readData() throws ReadFromStreamException;

}
