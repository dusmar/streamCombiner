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
public abstract class AbstractDataStreamDecorator implements DataStreamDecorator {

	private InputStream stream;
	private Data nextData;

	public AbstractDataStreamDecorator(InputStream stream) {
		this.stream = stream;
	}

	abstract protected Data getNextDataInner();

	@Override
	public Data nextData() {
		Data retData = null;
		try {
			retData = (Data) nextData.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.nextData = getNextDataInner();
		return retData;
	}

	protected void setNextData(Data data) {
		this.nextData = data;
	}

	
	@Override
	public boolean hasNextData() {
		return nextData != null;
	}


	@Override
	public InputStream getStream() {
		return stream;
	}

	public void close() throws IOException {
		getStream().close();
	}

}
