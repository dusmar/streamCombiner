package org.dm.streamcombiner.reader;

import java.io.InputStream;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;


/**
 * 
 * @author Dusan Maruscak
 *
 */
public interface DataStreamDecorator {
	
	public Data nextData() throws ReadFromStreamException;
	
	public boolean hasNextData();
	
	public InputStream getStream();
	
	
}
