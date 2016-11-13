package org.dm.streamcombiner.combiner.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dm.streamcombiner.combiner.Combiner;
import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;
import org.dm.streamcombiner.reader.impl.DataStreamDecoratorFactory;

/**
 * 
 * This is an implementation of algorithm to merge entries from all individual
 * inputs streams. It assumes that the entries in each input stream are sorted
 * by timestamp field in ascending order. First entry from each stream is
 * inserted into a heap. Using EXTRACT-MIN the smallest element X of the heap is
 * obtained and inserted in output stream. Assuming that X came from stream S,
 * then  the next element from stream S is taken  and inserted it into the heap.
 * Continuing in this fashion yields the merged stream
 *
 * <p>
 * Implementation note: this implementation provides time complexity log(k) * n
 * where k is number of streams and n is number of total elements.
 *
 * @author Dusan Maruscak
 */

public class MergeSortCombinerImpl implements Combiner {

	@Override
	public void combine(InputStream[] inputs, OutputStream output) {
		MergePriorityQueue heap = new MergePriorityQueue();
		initHeap(heap, inputs);
		while (!heap.isEmpty()) {
			MergeSortEntry entry = heap.poll();
			try {
				output.write(entry.getData().toJSONString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				// TODO error handling
			}
			if (entry.getDecorator().hasNextData()) {
				DataStreamDecorator decorator = entry.getDecorator();
				heap.add(new MergeSortEntry(decorator.nextData(), decorator));
			}
		}

	}

	private void initHeap(MergePriorityQueue heap, InputStream[] inputs) {
		for (int i = 0; i < inputs.length; ++i) {
			DataStreamDecorator decorator = DataStreamDecoratorFactory
					.getDataStreamDecorator(inputs[i]);
			if (decorator.hasNextData()) {
				heap.add(new MergeSortEntry(decorator.nextData(), decorator));
			}
		}
	}

	static class MergeSortEntry implements Comparable<MergeSortEntry> {
		private Data data;
		private DataStreamDecorator decorator;

		public MergeSortEntry(Data data, DataStreamDecorator decorator) {
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

		public DataStreamDecorator getDecorator() {
			return decorator;
		}

		public void setDecorator(DataStreamDecorator decorator) {
			this.decorator = decorator;
		}

		@Override
		public int compareTo(MergeSortEntry o) {
			return this.getData().getTimestamp()
					.compareTo(o.getData().getTimestamp());
		}

	}

}