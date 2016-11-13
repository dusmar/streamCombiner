package org.dm.streamcombiner.reader.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class StAXDataStreamDecorator implements DataStreamDecorator {

	public static final Integer MAX_NUMBER_OF_EVENTS_TO_FOUND_AMOUNT = 100;
	public static final String START_WRAP_TAG = "<root>";
	public static final String END_WRAP_TAG = "</root>";

	public static final String MESSAGE_ELEMENT_NAME = "data";
	public static final String TIMESTAMP_ELEMENT_NAME = "timestamp";
	public static final String AMOUNT_ELEMENT_NAME = "amount";

	private InputStream stream;
	private XMLEventReader eventReader;
	private Data nextData;

	public StAXDataStreamDecorator(InputStream stream) {
		this.stream = stream;
		init();
	}

	private void init() {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		List<InputStream> streams = Arrays.asList(new ByteArrayInputStream(
				START_WRAP_TAG.getBytes()), stream, new ByteArrayInputStream(
				END_WRAP_TAG.getBytes()));
		SequenceInputStream seqStream = new SequenceInputStream(
				Collections.enumeration(streams));

		try {
			this.eventReader = factory.createXMLEventReader(new BufferedReader(
					new InputStreamReader(seqStream)));
			this.nextData = parseDataFromStream();
		} catch (XMLStreamException e) {
			e.printStackTrace(); // TODO error
		}

	}

	private Data parseDataFromStream() {
		Data data = null;
		int numberOfEvent = 0;

		while (eventReader.hasNext()
				&& numberOfEvent < MAX_NUMBER_OF_EVENTS_TO_FOUND_AMOUNT) {
			try {
				XMLEvent event = eventReader.nextEvent();

				if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
					if (event.asStartElement().getName().getLocalPart()
							.equalsIgnoreCase(MESSAGE_ELEMENT_NAME)) {
						data = new Data();
					} else if (event.asStartElement().getName().getLocalPart()
							.equalsIgnoreCase(TIMESTAMP_ELEMENT_NAME)) {
						data.setTimestamp(Long.parseLong(getCurrentElementValue()));

					} else if (event.asStartElement().getName().getLocalPart()
							.equalsIgnoreCase(AMOUNT_ELEMENT_NAME)) {
						data.setAmount(getCurrentElementValue());

					}

					if (isInitialized(data)) {
						return data;
					}
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
				//TODO error handling
			}
			numberOfEvent++;
		}

		return null;
	}

	@Override
	public Data nextData()  {
		Data retData = null;
		try {
			retData = (Data) nextData.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.nextData = parseDataFromStream();
		return retData;
	}

	private boolean isInitialized(Data data) {
		return data != null && data.getAmount() != null
				&& data.getTimestamp() != null;
	}

	private String getCurrentElementValue() throws XMLStreamException {
		if (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			return event.asCharacters().getData();
		}
		throw new XMLStreamException();

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