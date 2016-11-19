package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Tests for {@link StxDataReader JAXBDataReader}. However all tests method are in parent class
 *
 * @author Dusan Maruscak
 *
 */
public class JAXBDataReaderTest extends DataReaderTest {

	@Override
	public JAXBDataReader getDecorator(String file) throws ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new JAXBDataReader(new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(file))));
	}
}
