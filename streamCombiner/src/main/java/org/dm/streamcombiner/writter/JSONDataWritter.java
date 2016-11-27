package org.dm.streamcombiner.writter;

import java.io.BufferedWriter;
import java.io.IOException;

import org.dm.streamcombiner.model.Data;

/**
 * Writes Data objects in JSON to a character-output stream
 * 
 * @author Dusan Maruscak
 *
 */
public class JSONDataWritter extends DataWritter {

	protected JSONDataWritter(BufferedWriter writter) {
		super(writter);
	}

	/**
	 * 
	 */
	@Override
	public void writeData(Data data) throws IOException {
		write(String.format("{ \"data\": { \"timestamp\":%d, \"amount\":\"%s\" }}%n", data.getTimestamp(),
				data.getAmount()));

	}

}
