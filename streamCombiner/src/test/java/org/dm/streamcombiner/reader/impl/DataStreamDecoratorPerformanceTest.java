package org.dm.streamcombiner.reader.impl;

import java.io.IOException;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataStreamDecoratorPerformanceTest {
	
	
	private static final int EXPECTED_NUMBER_OF_LINES = 70656;

	@Test
	public void testPerformacne() throws IOException, ReadFromStreamException{
		long staxTime= staxTest();
		System.out.println("staxTime: "+staxTime);
		long jaxbTime= jaxbTest();
		System.out.println("jaxbTime: "+jaxbTime);
		Assert.assertTrue(staxTime<jaxbTime);

		
	}
	
	public long staxTest() throws ReadFromStreamException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		StAXDataStreamDecorator decorator = new StAXDataStreamDecorator(
				classLoader.getResourceAsStream("DataPerformance.xml"));
		long start = System.currentTimeMillis();
		int i = 0;
		while (decorator.hasNextData()) {
			decorator.nextData();
			i++;
		}
		Assert.assertEquals(EXPECTED_NUMBER_OF_LINES, i);
		decorator.close();
		long end = System.currentTimeMillis();
		return end - start;
	}

	public long jaxbTest() throws ReadFromStreamException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		JAXBDataStreamDecorator decorator = new JAXBDataStreamDecorator(
				classLoader.getResourceAsStream("DataPerformance.xml"));
		long start = System.currentTimeMillis();
		int i = 0;
		while (decorator.hasNextData()) {
			decorator.nextData();
			i++;
		}
		Assert.assertEquals(EXPECTED_NUMBER_OF_LINES, i);
		decorator.close();
		long end = System.currentTimeMillis();
		return end - start;
	}
	


}
