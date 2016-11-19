package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * Reads Data objects from a buffered character-input stream. Uses Java
 * Streaming API for XML (Java StAX) to provide the efficient parsing of XML
 * fragments. Data objects are created from parsed fragments.
 * 
 * Streaming API for XML requires valid XML document to be streamed. According
 * to XML spec, an XML document must have a single root element. If input stream
 * contains just fragments, it may be wraped by (@see RootWrapInputStreamReader)
 * 
 *
 * @see FileReader
 * @see InputStreamReader
 * @see java.nio.file.Files#newBufferedReader
 *
 * @author Dusan Maruscak
 */

public class StAXDataReader extends DataReader {

	public static final Integer MAX_NUMBER_OF_EVENTS_TO_FOUND_AMOUNT = 100;

	public static final String MESSAGE_ELEMENT_NAME = "data";
	public static final String TIMESTAMP_ELEMENT_NAME = "timestamp";
	public static final String AMOUNT_ELEMENT_NAME = "amount";

	private XMLEventReader eventReader;

	public StAXDataReader(BufferedReader reader) throws ReadFromStreamException {
		super(reader);
		init(reader);
	}

	private void init(BufferedReader reader) throws ReadFromStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			this.eventReader = factory.createXMLEventReader(reader);
		} catch (XMLStreamException xe) {
			throw new ReadFromStreamException(xe);

		}

	}

	public Data readData() throws ReadFromStreamException {
		Data data = null;
		int numberOfEvent = 0;

		while (eventReader.hasNext() && numberOfEvent < MAX_NUMBER_OF_EVENTS_TO_FOUND_AMOUNT) {
			try {
				XMLEvent event = eventReader.nextEvent();

				if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
					if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(MESSAGE_ELEMENT_NAME)) {
						data = new Data();
					} else if (event.asStartElement().getName().getLocalPart()
							.equalsIgnoreCase(TIMESTAMP_ELEMENT_NAME)) {
						data.setTimestamp(Long.parseLong(getCurrentElementValue()));

					} else if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(AMOUNT_ELEMENT_NAME)) {
						data.setAmount(getCurrentElementValue());

					}

					if (isInitialized(data)) {
						return data;
					}
				}
			} catch (XMLStreamException e) {
				throw new ReadFromStreamException(e);
			}
			numberOfEvent++;
		}
		return null;
	}

	private boolean isInitialized(Data data) {
		return data != null && data.getAmount() != null && data.getTimestamp() != null;
	}

	private String getCurrentElementValue() throws XMLStreamException {
		if (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			return event.asCharacters().getData();
		}
		throw new XMLStreamException();

	}

}
