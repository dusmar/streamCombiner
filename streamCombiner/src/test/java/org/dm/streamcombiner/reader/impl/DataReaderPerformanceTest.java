package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * Tests performance of StAXDataReader vs. JAXBDataReader. Just one run is executed however Stax implementation should be always faster :) 
 * 
 * @author Dusan Maruscak
 *
 */
public class DataReaderPerformanceTest {

	private static final int EXPECTED_NUMBER_OF_LINES = 70656;

	@Test
	public void testPerformacne() throws IOException, ReadFromStreamException {
		long staxTime = staxTest();
		System.out.println("staxTime: " + staxTime);
		long jaxbTime = jaxbTest();
		System.out.println("jaxbTime: " + jaxbTime);
		Assert.assertTrue(staxTime < jaxbTime);

	}

	public long staxTest() throws ReadFromStreamException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		StAXDataReader decorator = new StAXDataReader(new BufferedReader(
				new RootWrapInputStreamReader(classLoader.getResourceAsStream("DataPerformance.xml"))));

		long start = System.currentTimeMillis();
		int i = 0;
		while (decorator.readData() != null) {
			i++;
		}
		Assert.assertEquals(EXPECTED_NUMBER_OF_LINES, i);
		decorator.close();
		long end = System.currentTimeMillis();
		return end - start;
	}

	public long jaxbTest() throws ReadFromStreamException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		JAXBDataReader decorator = new JAXBDataReader(
				new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("DataPerformance.xml"))));

		long start = System.currentTimeMillis();
		int i = 0;
		while (decorator.readData() != null) {
			i++;
		}
		Assert.assertEquals(EXPECTED_NUMBER_OF_LINES, i);
		decorator.close();
		long end = System.currentTimeMillis();
		return end - start;
	}

}
