package org.dm.streamcombiner.reader.impl;

import java.io.IOException;

import org.dm.streamcombiner.model.Data;
import org.junit.Test;

public class StAXDataStreamDecoratorTest {

	@Test
	public void combineTest() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		StAXDataStreamDecorator decorator = new StAXDataStreamDecorator(classLoader.getResourceAsStream("Data1.xml"));
		while (decorator.hasNextData()) {
			Data data = decorator.nextData();
			System.out.print(data.toJSONString());
		}

	}

}
