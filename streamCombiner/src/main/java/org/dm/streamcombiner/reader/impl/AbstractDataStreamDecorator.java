package org.dm.streamcombiner.reader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBException;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

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

	abstract protected Data getNextDataInner() throws ReadFromStreamException;

	@Override
	public Data nextData() throws ReadFromStreamException {
		if (nextData==null) {
			throw new NoSuchElementException();
		}
		Data retData = null;
		try {
			retData = (Data) nextData.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
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
