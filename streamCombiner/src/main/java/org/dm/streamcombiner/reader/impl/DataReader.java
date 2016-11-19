package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Reads Data objects from  a buffered character-input stream
 *
 *
 *
 * @see FileReader
 * @see InputStreamReader
 * @see java.nio.file.Files#newBufferedReader
 *
 * @author Dusan Maruscak
 */

public abstract class DataReader extends BufferedReader {

	public DataReader(BufferedReader reader) {
		super(reader);
	}

	public abstract Data readData() throws ReadFromStreamException;

}
