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
	 * Algorithm to merge entries from all individual inputs streams.
	 * 
	 * @param inputs
	 *            array of input stream to combine. Input streams provide data
	 *            in XML format. Data in each stream are sorted by timestamp.
	 *            Sample of XML format:
	 * 
	 *            <pre>
	 * 			{@code <data> 
	 * 			  <timestamp>123456789</timeStamp>
	 *          	          <amount>1234.567890</amount> 
	 *                        </data> }
	 *            </pre>
	 * 
	 * @param output
	 *            combined stream. Output is JSON stream. Data in output stream
	 *            are sorted by timestamp. If several inputs provide data with
	 *            the same timestamp - amounts are merged here.
	 *            Sample of JSON format:
	 * 
	 *            <pre>
	 * 			  {@code { "data": {"timestamp": 123456789, "amount":"1234.567890" }} }
	 *            </pre>
	 * 
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	public void combine(InputStream[] inputs, OutputStream output) throws IOException, ReadFromStreamException;

}
