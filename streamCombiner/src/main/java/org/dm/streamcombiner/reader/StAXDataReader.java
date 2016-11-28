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

	/**
	 * Reads Data object from stream. Iterates through StAX events. If start
	 * element <data> is found, new Data Object is created. Then corresponding
	 * events are handled and Data Object is initialized.
	 * 
	 * @return Data Object or null if the end of the stream has been reached
	 * @throws ReadFromStreamException
	 *             If an I/O error occurs or Data object cannot be parsed
	 */
	public Data readData() throws ReadFromStreamException {
		Data data = null;

		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();

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
