package org.dm.streamcombiner.combiner;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

public class MergePriorityQueueTest {

	/**
	 * Add two items (no merge), then call 3 times poll (last one should return
	 * null)
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void addNoMerge() throws ReadFromStreamException {
		Data data1 = new Data();
		data1.setAmount("1");
		data1.setTimestamp(1L);

		Data data2 = new Data();
		data2.setAmount("2");
		data2.setTimestamp(2L);

		MergeSortCombiner.MergePriorityQueue queue = new MergeSortCombiner.MergePriorityQueue();
		queue.add(new MergeSortCombiner.MergeSortEntry(data1, null));
		queue.add(new MergeSortCombiner.MergeSortEntry(data2, null));

		MergeSortCombiner.MergeSortEntry entry1 = queue.poll();
		Assert.assertNotNull(entry1);
		Assert.assertEquals(1L, entry1.getData().getTimestamp().longValue());

		MergeSortCombiner.MergeSortEntry entry2 = queue.poll();
		Assert.assertNotNull(entry2);
		Assert.assertEquals(2L, entry2.getData().getTimestamp().longValue());

		
		MergeSortCombiner.MergeSortEntry entry3 = queue.poll();
		Assert.assertNull(entry3);

	}
	
	
	/**
	 * Add three items (1. and 2. are merged), then call 3 times poll (last one should return
	 * null)
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void addMerge() throws ReadFromStreamException {
		Data data1 = new Data();
		data1.setAmount("1");
		data1.setTimestamp(1L);

		Data data2 = new Data();
		data2.setAmount("2");
		data2.setTimestamp(1L);


		Data data3 = new Data();
		data3.setAmount("3");
		data3.setTimestamp(3L);

		
		MergeSortCombiner.MergePriorityQueue queue = new MergeSortCombiner.MergePriorityQueue();
		queue.add(new MergeSortCombiner.MergeSortEntry(data1, null));
		queue.add(new MergeSortCombiner.MergeSortEntry(data2, null));
		queue.add(new MergeSortCombiner.MergeSortEntry(data3, null));

		MergeSortCombiner.MergeSortEntry entry1 = queue.poll();
		Assert.assertNotNull(entry1);
		Assert.assertEquals(1L, entry1.getData().getTimestamp().longValue());
		Assert.assertEquals("3", entry1.getData().getAmount());

		
		MergeSortCombiner.MergeSortEntry entry2 = queue.poll();
		Assert.assertNotNull(entry2);
		Assert.assertEquals(3L, entry2.getData().getTimestamp().longValue());

		
		MergeSortCombiner.MergeSortEntry entry3 = queue.poll();
		Assert.assertNull(entry3);

	}


}
