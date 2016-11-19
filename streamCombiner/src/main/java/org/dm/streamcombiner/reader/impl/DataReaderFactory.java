package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.InputStream;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class DataReaderFactory {

	public static DataReader getDataReader(InputStream input) throws ReadFromStreamException {
		return new StAXDataReader(new BufferedReader(new RootWrapInputStreamReader(input)));
	}

}
