package org.dm.streamcombiner.reader.impl;

import java.io.InputStream;

import org.dm.streamcombiner.reader.DataStreamDecorator;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class DataStreamDecoratorFactory {

	public static DataStreamDecorator getDataStreamDecorator(InputStream input) {
		return new StAXDataStreamDecorator(input);

	}

}
