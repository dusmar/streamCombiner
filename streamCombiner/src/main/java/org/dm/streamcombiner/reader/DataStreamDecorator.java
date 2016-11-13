package org.dm.streamcombiner.reader;

import java.io.InputStream;

import org.dm.streamcombiner.model.Data;


/**
 * 
 * @author Dusan Maruscak
 *
 */
public interface DataStreamDecorator {
	
	public Data nextData();
	
	public boolean hasNextData();
	
	public InputStream getStream();
	
	
}
