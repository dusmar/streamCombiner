package org.dm.streamcombiner.combiner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataReader;
import org.dm.streamcombiner.reader.DataReaderFactory;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.dm.streamcombiner.writter.DataWritter;
import org.dm.streamcombiner.writter.DataWritterFactory;

/**
 * 
 * Contains implementation of {@link Combiner} based on merge sort.
 *
 * @author Dusan Maruscak
 */

public class MergeSortCombiner implements Combiner {

	private static final Logger LOGGER = Logger.getLogger(MergeSortCombiner.class.getName());

	protected MergeSortCombiner() {
		super();
	}

	/**
	 * Stream merge sort implementation. Algorithm has two main steps:
	 * <p>
	 * Step 1 (queue initialization)
	 * <p>
	 * For each stream, reads Data Objects from stream until such Data Object is
	 * found that true is result of call MergePriorityQueue.add(new
	 * MergeSortCombiner.MergeSortEntry(DataObject, Stream)). It means that Data
	 * Object is added to queue without the merge.
	 * <p>
	 * Step 2 (merging)
	 * <p>
	 * Using EXTRACT-MIN the smallest element X of the queue is obtained and
	 * written into output stream. Assuming that X came from stream S, then
	 * reads Data Objects from S, until such Data Object is found that true is
	 * result of call MergePriorityQueue.add(new
	 * MergeSortCombiner.MergeSortEntry(DataObject, Stream)). Continuing in this
	 * fashion yields the merged stream.
	 * 
	 * <p>
	 * Implementation note: this implementation provides time complexity log(k)
	 * n where k is number of streams and n is number of total elements.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs while writing to output stream
	 * 
	 * 
	 */
	@Override
	public void combine(InputStream[] inputs, OutputStream output) throws IOException {
		MergePriorityQueue heap = new MergePriorityQueue();
		initQueue(heap, inputs); // Step 1
		DataWritter writter = DataWritterFactory.getDataWritter(output);
		while (!heap.isEmpty()) { // Step 2
			MergeSortEntry entry = heap.poll();
			writter.writeData(entry.getData());
			DataReader decorator = entry.getDecorator();
			insertNewEntryIntoHeap(decorator, heap);
		}
		writter.closeDataWritter(); // close wrapped stream
	}

	/**
	 * For each stream, reads Data Objects from stream until such Data Object is
	 * found that true is result of call MergePriorityQueue.add(new
	 * MergeSortCombiner.MergeSortEntry(DataObject, Stream)). It means that Data
	 * Object is added to queue without the merge.
	 * 
	 * @param queue
	 * @param inputs
	 */
	private void initQueue(MergePriorityQueue queue, InputStream[] inputs) {
		for (int i = 0; i < inputs.length; ++i) {
			DataReader decorator = null;
			try {
				decorator = DataReaderFactory.getDataReader(inputs[i]);
			} catch (ReadFromStreamException e) {
				LOGGER.warning("Error while reading  or parsing input stream. It will be further ommited.");
			}
			insertNewEntryIntoHeap(decorator, queue);
		}
	}

	/**
	 * Reads Data Objects from stream, until such Data Object is found that true
	 * is result of call MergePriorityQueue.add(new
	 * MergeSortCombiner.MergeSortEntry(DataObject, Stream)).
	 * 
	 * 
	 * @param decorator
	 * @param queue
	 */
	private void insertNewEntryIntoHeap(DataReader decorator, MergePriorityQueue queue) {
		Data data = null;
		try {
			while ((data = decorator.readData()) != null && !queue.add(new MergeSortEntry(data, decorator))) {
			}
		} catch (ReadFromStreamException e) {
			LOGGER.warning("Error while reading  or parsing input stream. It will be further ommited.");
		}

	}

	/**
	 * Wraps {@link java.util.PriorityQueue java.util.PriorityQueue}
	 * 
	 * <p>
	 * Reduces original interface just to {@link #poll() poll} and
	 * {@link #add(MergeSortEntry) add} operations
	 * 
	 * <p>
	 * Assuming that X is entry to be added to queue. If queue contains an entry
	 * Y such that Y.data.timestamp = X.data.timestamp, then entries are
	 * {@link #merge(MergeSortEntry, MergeSortEntry) merged}, else X is added.
	 * An internal Map {@link #timeStampIndices} is used to speed up that
	 * checking, so the merge has no negative impact on add and poll time
	 * complexity.
	 * 
	 * @author Dusan Maruscak
	 *
	 */
	static class MergePriorityQueue {

		private Map<Long, MergeSortCombiner.MergeSortEntry> timeStampIndices;
		private PriorityQueue<MergeSortCombiner.MergeSortEntry> innerPriorityQueue;

		public MergePriorityQueue() {
			super();
			this.innerPriorityQueue = new PriorityQueue<MergeSortCombiner.MergeSortEntry>();
			this.timeStampIndices = new HashMap<Long, MergeSortCombiner.MergeSortEntry>();
		}

		private MergeSortCombiner.MergeSortEntry findByTimestamp(long timestamp) {
			return timeStampIndices.get(timestamp);
		}

		/**
		 * 
		 * @return delegates call to <tt>innerPriorityQueue.isEmpty()</tt>.
		 */
		public boolean isEmpty() {
			return innerPriorityQueue.isEmpty();
		}

		/**
		 * 
		 * @return
		 */
		public MergeSortCombiner.MergeSortEntry poll() {
			MergeSortCombiner.MergeSortEntry mergeSortEntry = innerPriorityQueue.poll();
			if (mergeSortEntry != null) {
				timeStampIndices.remove(mergeSortEntry);
			}
			return mergeSortEntry;
		}

		/**
		 * 
		 * Assuming that mergeSortEntry is entry to be added to queue. If queue
		 * contains an entry Y such that Y.data.timestamp =
		 * mergeSortEntry.data.timestamp, then entries are
		 * {@link #merge(MergeSortEntry, MergeSortEntry) merged}, else
		 * mergeSortEntry is added.
		 * 
		 * @param mergeSortEntry
		 * @return true if entry is really added, false otherwise
		 */
		public boolean add(MergeSortCombiner.MergeSortEntry mergeSortEntry) {
			MergeSortCombiner.MergeSortEntry entry = findByTimestamp(mergeSortEntry.getData().getTimestamp());
			if (entry != null) {
				merge(entry, mergeSortEntry);
				return false;
			} else {
				timeStampIndices.put(mergeSortEntry.getData().getTimestamp(), mergeSortEntry);
				innerPriorityQueue.add(mergeSortEntry);
				return true;
			}

		}

		/**
		 * Add toBeAddedEntry.data.amount to queueEntry.data.amount. Assuming
		 * that toBeAddedEntry.data.timestamp = queueEntry.data.timestamp.
		 * 
		 * 
		 * @param queueEntry
		 *            entry already presented in queue
		 * @param toBeAddedEntry
		 *            entry which should be merged with an existing entry
		 */
		protected void merge(MergeSortEntry queueEntry, MergeSortEntry toBeAddedEntry) {
			queueEntry.getData().setAmount(new BigDecimal(queueEntry.getData().getAmount())
					.add(new BigDecimal(toBeAddedEntry.getData().getAmount())).toString());

		}

	}

	/**
	 * 
	 * Holds Data object and reference to stream it was originally read from.
	 * 
	 * @author dmarusca
	 *
	 */
	static class MergeSortEntry implements Comparable<MergeSortEntry> {
		private Data data;
		private DataReader decorator;

		public MergeSortEntry(Data data, DataReader decorator) {
			super();
			this.data = data;
			this.decorator = decorator;
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public DataReader getDecorator() {
			return decorator;
		}

		public void setDecorator(DataReader decorator) {
			this.decorator = decorator;
		}

		@Override
		public int compareTo(MergeSortEntry o) {
			return this.getData().getTimestamp().compareTo(o.getData().getTimestamp());
		}

	}

}