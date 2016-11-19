package org.dm.streamcombiner.combiner.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dm.streamcombiner.combiner.Combiner;
import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.dm.streamcombiner.reader.impl.DataReader;
import org.dm.streamcombiner.reader.impl.DataReaderFactory;

/**
 * 
 * This class contains implementation of {@link Combiner} based on merge sort.
 * First entry from each stream is inserted into a heap. Using EXTRACT-MIN the
 * smallest element X of the heap is obtained and written into output stream.
 * Assuming that X came from stream S, then the next element from stream S is
 * taken and inserted into the heap. Continuing in this fashion yields the
 * merged stream
 *
 * <p>
 * Implementation note: this implementation provides time complexity log(k) * n
 * where k is number of streams and n is number of total elements.
 *
 * @author Dusan Maruscak
 */

public class MergeSortCombinerImpl implements Combiner {
	
	protected MergeSortCombinerImpl(){
		super();
	}
	

	/**
	 * @throws IOException
	 * @inheritDoc
	 */
	@Override
	public void combine(InputStream[] inputs, OutputStream output) throws IOException {
		MergePriorityQueue heap = new MergePriorityQueue();
		initHeap(heap, inputs);
		while (!heap.isEmpty()) {
			MergeSortEntry entry = heap.poll();
			output.write(entry.getData().toJSONString().getBytes());
			DataReader decorator = entry.getDecorator();
			try {
				insertNewEntryIntoHeap(decorator, heap);
			} catch (ReadFromStreamException e) {
				// TODO error handling
			}
		}
	}


	private void initHeap(MergePriorityQueue heap, InputStream[] inputs) {
		for (int i = 0; i < inputs.length; ++i) {
			DataReader decorator;
			try {
				decorator = DataReaderFactory.getDataReader(inputs[i]);
				insertNewEntryIntoHeap(decorator, heap);
			} catch (ReadFromStreamException e) {
				// TODO error handling
			}
		}
	}

	private void insertNewEntryIntoHeap(DataReader decorator, MergePriorityQueue heap) throws ReadFromStreamException {
		Data data = null;
		while ((data = decorator.readData()) != null && !heap.add(new MergeSortEntry(data, decorator))) {
		}

	}

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