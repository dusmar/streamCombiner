package org.dm.streamcombiner.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Assumes that characters in InputStream are encoded by UTF-8
 * @author Dusan Maruscak
 *
 */
public class DataReaderFactory {

	public static DataReader getDataReader(InputStream input) throws ReadFromStreamException {
		return new StAXDataReader(new BufferedReader(new RootWrapInputStreamReader(input, StandardCharsets.UTF_8)));
	}

}
