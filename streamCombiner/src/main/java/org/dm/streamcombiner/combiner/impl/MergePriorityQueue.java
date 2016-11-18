package org.dm.streamcombiner.combiner.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.dm.streamcombiner.combiner.impl.MergeSortCombinerImpl.MergeSortEntry;

/**
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

	public MergeSortCombinerImpl.MergeSortEntry poll() {
		MergeSortCombinerImpl.MergeSortEntry mergeSortEntry = innerPriorityQueue.poll();
		if (mergeSortEntry!=null) {
			timeStampIndices.remove(mergeSortEntry);
		}
		return mergeSortEntry;
	}

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

	private void merge(MergeSortEntry entry, MergeSortEntry mergeSortEntry) {
		entry.getData().setAmount(new BigDecimal(entry.getData().getAmount())
				.add(new BigDecimal(mergeSortEntry.getData().getAmount())).toString());

	}

}
