package org.dm.streamcombiner.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Reads Data objects from a buffered character-input stream. Uses JAXB API to
 * convert XML fragments to Data objects. Assumes that input stream contains
 * just fragments, no root element is presented in.
 * 
 * 
 * @author Dusan Maruscak
 *
 */
public class JAXBDataReader extends DataReader {

	private Unmarshaller unmarshaller;

	protected JAXBDataReader(BufferedReader reader) throws ReadFromStreamException {
		super(reader);
		init();
	}

	private void init() throws ReadFromStreamException {
		try {
			JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(Data.class);
			this.unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new ReadFromStreamException(e);
		}
	}

	public Data readData() throws ReadFromStreamException {
		String line = null;
		try {
			if ((line = readLine()) != null) {
				StringReader reader = new StringReader(line);
				return (Data) unmarshaller.unmarshal(reader);
			}
		} catch (IOException | JAXBException e) {
			throw new ReadFromStreamException(e);

		}
		return null;

	}

}
