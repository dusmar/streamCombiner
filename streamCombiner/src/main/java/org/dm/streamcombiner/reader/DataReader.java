package org.dm.streamcombiner.reader;

import java.io.BufferedReader;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Reads Data objects from a buffered character-input stream
 *
 *
 *
 *
 * @author Dusan Maruscak
 */

public abstract class DataReader extends BufferedReader {

	public DataReader(BufferedReader reader) {
		super(reader);
	}

	public abstract Data readData() throws ReadFromStreamException;

}
