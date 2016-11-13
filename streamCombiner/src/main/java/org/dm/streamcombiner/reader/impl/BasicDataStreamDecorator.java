package org.dm.streamcombiner.reader.impl;

import java.io.IOException;
import java.io.InputStream;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class BasicDataStreamDecorator implements DataStreamDecorator {

	private InputStream stream;
	private Data currentData;

	protected BasicDataStreamDecorator(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public Data nextData() {
		Data data = null;
		int numberOfEvent = 0;
		return data;
	}

	private Data getDataFromLine(String line) {
		return currentData;

	}

	@Override
	public boolean hasNextData() {
		return currentData != null;
	}

	@Override
	public InputStream getStream() {
		return stream;
	}

	public void close() throws IOException {
		getStream().close();
	}

}
