package org.dm.streamcombiner.reader.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.stream.XMLStreamException;

public class StAXDataStreamDecoratorTest  {


	public static void main(String[] args) throws IOException,
			XMLStreamException {
		URL url = new File(StAXDataStreamDecorator.class
				.getResource("Data.xml").getFile()).toURI().toURL();
		URLConnection conn = url.openConnection();
		StAXDataStreamDecorator decorator = new StAXDataStreamDecorator(
				conn.getInputStream());
		while (decorator.hasNextData()) {
			decorator.nextData();
		}
	}

}
