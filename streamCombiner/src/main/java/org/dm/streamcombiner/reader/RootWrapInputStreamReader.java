package org.dm.streamcombiner.reader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Simple decorator of InputStreamReader. The StAX API does not support
 * fragments (@see StAXDataReader). RootWrapInputStreamReader wraps content of
 * InputStreamReader with a root element
 * 
 * @author Dusan Maruscak
 */

public class RootWrapInputStreamReader extends InputStreamReader {

	public static final String START_WRAP_TAG = "<root>";
	public static final String END_WRAP_TAG = "</root>";

	public RootWrapInputStreamReader(InputStream in, Charset charset) {
		super(wrap(in), charset);
	}

	private static InputStream wrap(InputStream in) {
		List<InputStream> streams = Arrays.asList(new ByteArrayInputStream(START_WRAP_TAG.getBytes()), in,
				new ByteArrayInputStream(END_WRAP_TAG.getBytes()));
		return new SequenceInputStream(Collections.enumeration(streams));
	}

}
