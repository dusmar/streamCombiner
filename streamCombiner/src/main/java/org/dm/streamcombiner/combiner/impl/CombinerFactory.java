package org.dm.streamcombiner.combiner.impl;

import org.dm.streamcombiner.combiner.Combiner;

public class CombinerFactory {

	public static Combiner getCombiner() {
		return new MergeSortCombinerImpl();
	}

}
