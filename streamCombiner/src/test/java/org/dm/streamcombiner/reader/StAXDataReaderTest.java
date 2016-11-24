package org.dm.streamcombiner.reader;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;


/**
 * Tests for {@link StxDataReader StxDataReader}. However all tests method are in parent class
 *
 * @author Dusan Maruscak
 *
 */
public class StAXDataReaderTest extends DataReaderTest {

	@Override
	public StAXDataReader getDecorator(String file) throws ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new StAXDataReader(new BufferedReader(new RootWrapInputStreamReader(classLoader.getResourceAsStream(file), StandardCharsets.UTF_8)));
	}

}
