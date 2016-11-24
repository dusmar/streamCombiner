package org.dm.streamcombiner.writter;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Dusan Maruscak
 *
 */
public class DataWritterFactory {

	public static DataWritter getDataWritter(OutputStream output) {
		return new JSONDataWritter(new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8)));
	}

}
