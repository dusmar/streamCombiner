package org.dm.streamcombiner.combiner.impl;

import java.io.IOException;
import java.io.InputStream;

import org.dm.streamcombiner.combiner.Combiner;
import org.junit.Test;



public class MergeSortCombinerImplTest  {

	Combiner comb= new MergeSortCombinerImpl();
	
    @Test
	public void combineTest() throws IOException {
    	ClassLoader classLoader = getClass().getClassLoader();

		comb.combine(new InputStream[] {classLoader
				.getResourceAsStream("Data1.xml"), classLoader
				.getResourceAsStream("Data2.xml"), classLoader
				.getResourceAsStream("Data3.xml")}, System.out);
		

		
	}

}