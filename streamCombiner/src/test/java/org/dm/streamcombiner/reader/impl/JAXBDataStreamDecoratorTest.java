package org.dm.streamcombiner.reader.impl;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

public class JAXBDataStreamDecoratorTest extends AbstractDataStreamDecoratorTest {

	@Override
	public JAXBDataStreamDecorator getDecorator(String file) throws ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new JAXBDataStreamDecorator(classLoader.getResourceAsStream(file));
	}
}
