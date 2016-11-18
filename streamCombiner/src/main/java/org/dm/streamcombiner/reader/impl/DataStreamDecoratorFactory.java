package org.dm.streamcombiner.reader.impl;

import java.io.InputStream;

import org.dm.streamcombiner.reader.DataStreamDecorator;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class DataStreamDecoratorFactory {

	public static DataStreamDecorator getDataStreamDecorator(InputStream input) throws ReadFromStreamException {
		return new StAXDataStreamDecorator(input);

	}

}
