package org.dm.streamcombiner.combiner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * 
 * 
 * @author Dusan Maruscak
 *
 */
public interface Combiner {

	/**
	 * 
	 * Algorithm to merge entries from all individual inputs streams. Both input
	 * and output streams are sorted by timestamp field. If several inputs
	 * provide data with the same timestamp - amounts are merged. 
	 *
	 * @param inputs
	 *            array of input stream to combine. Input streams provide data
	 *            in XML format. Definition of XML format: <data>
	 *            <timestamp>123456789</timeStamp> <amount>1234.567890</amount>
	 *            </data>
	 * 
	 * @param output
	 *            combined stream. Output is JSON stream.  Definition of JSON format: { "data":
	 *            {timestamp":123456789, "amount":"1234.567890" }}
	 * @throws IOException 
	 */
	public void combine(InputStream[] inputs, OutputStream output) throws IOException;

}
