package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

public class StAXDataReaderTest extends DataReaderTest {

	@Override
	public StAXDataReader getDecorator(String file) throws ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new StAXDataReader(new BufferedReader(new RootWrapInputStreamReader(classLoader.getResourceAsStream(file))));
	}

}
