package org.dm.streamcombiner.combiner;

/**
 * 
 * 
 * @author Dusan Maruscak
 *
 */
public class CombinerFactory {

	public static Combiner getCombiner() {
		return new MergeSortCombiner();
	}

}
