package org.dm.streamcombiner.reader;

import java.io.BufferedReader;

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
 * contains just fragments, it may be wraped by {@link RootWrapInputStreamReader
 * RootWrapInputStreamReader}
 * 
 *
 *
 * @author Dusan Maruscak
 */

public class StAXDataReader extends DataReader {

	public static final Integer MAX_NUMBER_OF_EVENTS_TO_CHECK = 100;

	public static final String DATA_ELEMENT_NAME = "data";
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

		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				numberOfEvent++;

				if (isDataStart(event)) {
					data = new Data();
				}

				if (isTimestampStart(event)) {
					data.setTimestamp(Long.parseLong(getCurrentElementValue()));
				}

				if (isAmountStart(event)) {
					data.setAmount(getCurrentElementValue());
				}

				if (isDataEnd(event)) {
					if (isInitialized(data)) {
						return data;
					}
				}
				
				if (numberOfEvent == MAX_NUMBER_OF_EVENTS_TO_CHECK) {
					throw new ReadFromStreamException("Maximum number of events per single data object reached");
				}

			} catch (XMLStreamException e) {
				throw new ReadFromStreamException(e);
			}
		}
		return null;
	}

	private boolean isTimestampStart(XMLEvent event) {
		return isStartElement(event, TIMESTAMP_ELEMENT_NAME);
	}

	private boolean isAmountStart(XMLEvent event) {
		return isStartElement(event, AMOUNT_ELEMENT_NAME);
	}

	private boolean isDataStart(XMLEvent event) {
		return isStartElement(event, DATA_ELEMENT_NAME);
	}

	private boolean isStartElement(XMLEvent event, String elementName) {
		return (event.getEventType() == XMLStreamConstants.START_ELEMENT)
				&& (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(elementName));

	}

	private boolean isEndElement(XMLEvent event, String elementName) {
		return (event.getEventType() == XMLStreamConstants.END_ELEMENT)
				&& (event.asEndElement().getName().getLocalPart().equalsIgnoreCase(elementName));

	}

	private boolean isDataEnd(XMLEvent event) {
		return isEndElement(event, DATA_ELEMENT_NAME);

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
