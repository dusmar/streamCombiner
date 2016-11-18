package org.dm.streamcombiner.reader.impl;

import java.util.NoSuchElementException;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.DataStreamDecorator;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dusan Maruscak
 *
 */
public abstract class AbstractDataStreamDecoratorTest {

	/**
	 * Tests hasNextData on an empty stream (returns false)
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void hasNextDataEmptyStreamTest() throws ReadFromStreamException {
		Assert.assertEquals(false, getDecorator("DataEmpty.xml").hasNextData());
	}

	/**
	 * Tests nextData() on an empty stream (throws exception)
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test(expected = NoSuchElementException.class)
	public void nextDataEmptyStreamTest() throws ReadFromStreamException {
		getDecorator("DataEmpty.xml").nextData();
	}

	/**
	 * Tests hasNextData/nextData on a stream with one item: hasNextData returns
	 * true, nextData returns the item, hasNextData returns false, twice
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void hasNextDataWithOneItemStreamTest() throws ReadFromStreamException {
		DataStreamDecorator decorator = getDecorator("DataOneItem.xml");
		testItemFully(123456781L, "1231.567890", decorator);
		Assert.assertEquals(false, decorator.hasNextData());
		Assert.assertEquals(false, decorator.hasNextData());
	}

	/**
	 * Tests a stream with several items, making sure the iterator goes through
	 * each item, in the correct order
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void nextDataWithSeveralItemStreamTest() throws ReadFromStreamException {
		DataStreamDecorator decorator = getDecorator("Data1.xml");
		testItemFully(123456781L, "1231.567890", decorator);
		testItemFully(123456784L, "1234.567890", decorator);
		testItemFully(123456787L, "1236.567890", decorator);
		Assert.assertEquals(false, decorator.hasNextData());
	}

	/**
	 * Tests hasNextData (expects true) and nextData (compare result with expected values) 
	 * 
	 * @param expectedTimestamp
	 * @param expectedAmount
	 * @param decorator
	 * @throws ReadFromStreamException
	 */
	private void testItemFully(long expectedTimestamp, String expectedAmount, DataStreamDecorator decorator) throws ReadFromStreamException {
		Assert.assertEquals(true, decorator.hasNextData());
		Data data1 = decorator.nextData();
		Assert.assertEquals(expectedAmount, data1.getAmount());
		Assert.assertEquals(expectedTimestamp, data1.getTimestamp().longValue());
	}

	public abstract DataStreamDecorator getDecorator(String file) throws ReadFromStreamException;

}
