package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class JAXBDataStreamDecorator extends AbstractDataStreamDecorator implements DataStreamDecorator {

	private Unmarshaller unmarshaller;
	private BufferedReader in;

	protected JAXBDataStreamDecorator(InputStream stream) throws ReadFromStreamException {
		super(stream);
		init();
	}

	private void init() throws ReadFromStreamException {
		try {
			JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(Data.class);
			this.unmarshaller = jaxbContext.createUnmarshaller();
			this.in = new BufferedReader(new InputStreamReader(getStream()));
			setNextData(getNextDataInner());
		} catch (ReadFromStreamException e) {
			throw e;
		} catch (JAXBException e) {
			throw new ReadFromStreamException(e);
		}
	}

	protected Data getNextDataInner() throws ReadFromStreamException {
		String line = null;
		try {
			if ((line = in.readLine()) != null) {
				StringReader reader = new StringReader(line);
				return (Data) unmarshaller.unmarshal(reader);
			}
		} catch (IOException | JAXBException e) {
			throw new ReadFromStreamException(e);

		}
		return null;

	}

}
