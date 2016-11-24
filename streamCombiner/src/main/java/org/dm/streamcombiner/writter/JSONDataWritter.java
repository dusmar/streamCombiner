package org.dm.streamcombiner.writter;

import java.io.BufferedWriter;
import java.io.IOException;

import org.dm.streamcombiner.model.Data;

public class JSONDataWritter extends DataWritter {

	protected JSONDataWritter(BufferedWriter writter) {
		super(writter);
	}

	@Override
	public void writeData(Data data) throws IOException {
		write(String.format("{ \"data\": { \"timestamp\":%d, \"amount\":\"%s\" }}%n", data.getTimestamp(),
				data.getAmount()));

	}

}
