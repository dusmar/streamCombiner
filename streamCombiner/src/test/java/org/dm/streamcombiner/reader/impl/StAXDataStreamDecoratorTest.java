package org.dm.streamcombiner.reader.impl;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

public class StAXDataStreamDecoratorTest extends AbstractDataStreamDecoratorTest {

	@Override
	public StAXDataStreamDecorator getDecorator(String file) throws ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new StAXDataStreamDecorator(classLoader.getResourceAsStream(file));
	}

}
