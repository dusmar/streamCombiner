package org.dm.streamcombiner.combiner;

public class CombinerFactory {

	public static Combiner getCombiner() {
		return new MergeSortCombiner();
	}

}
