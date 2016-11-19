package org.dm.streamcombiner.combiner.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.dm.streamcombiner.combiner.impl.MergeSortCombinerImpl.MergeSortEntry;

/**
 * Wraps {@link java.util.PriorityQueue java.util.PriorityQueue}
 * 
 * <p>
 * Reduces original interface just to {@link #poll() poll} and
 * {@link #add(MergeSortEntry) add} operations
 * 
 * <p>
 * Assuming that X is entry to be added to queue. If queue contains an entry Y
 * such that Y.Data.timestamp = X.Data.timestamp, then entries are 
 * {@link #merge(MergeSortEntry, MergeSortEntry) merged}, else X is addded. An
 * internal {@link #timeStampIndices} is used to speed up that checking, so the
 * merge has no negative impact on add and poll time complexity.
 *  
 * @author Dusan Maruscak
 *
 */
public class MergePriorityQueue {

	private Map<Long, MergeSortCombinerImpl.MergeSortEntry> timeStampIndices;
	private PriorityQueue<MergeSortCombinerImpl.MergeSortEntry> innerPriorityQueue;

	public MergePriorityQueue() {
		super();
		this.innerPriorityQueue = new PriorityQueue<MergeSortCombinerImpl.MergeSortEntry>();
		this.timeStampIndices = new HashMap<Long, MergeSortCombinerImpl.MergeSortEntry>();
	}

	private MergeSortCombinerImpl.MergeSortEntry findByTimestamp(long timestamp) {
		return timeStampIndices.get(timestamp);
	}

	public boolean isEmpty() {
		return innerPriorityQueue.isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public MergeSortCombinerImpl.MergeSortEntry poll() {
		MergeSortCombinerImpl.MergeSortEntry mergeSortEntry = innerPriorityQueue.poll();
		if (mergeSortEntry != null) {
			timeStampIndices.remove(mergeSortEntry);
		}
		return mergeSortEntry;
	}

	/**
	 * 
	 * @param mergeSortEntry
	 * @return
	 */
	public boolean add(MergeSortCombinerImpl.MergeSortEntry mergeSortEntry) {
		MergeSortCombinerImpl.MergeSortEntry entry = findByTimestamp(mergeSortEntry.getData().getTimestamp());
		if (entry != null) {
			merge(entry, mergeSortEntry);
			return false;
		} else {
			timeStampIndices.put(mergeSortEntry.getData().getTimestamp(), mergeSortEntry);
			innerPriorityQueue.add(mergeSortEntry);
			return true;
		}

	}

	protected void merge(MergeSortEntry entry, MergeSortEntry mergeSortEntry) {
		entry.getData().setAmount(new BigDecimal(entry.getData().getAmount())
				.add(new BigDecimal(mergeSortEntry.getData().getAmount())).toString());

	}

}
