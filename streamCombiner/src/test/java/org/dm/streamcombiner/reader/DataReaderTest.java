package org.dm.streamcombiner.reader;

import org.dm.streamcombiner.model.Data;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dusan Maruscak
 *
 */
public abstract class DataReaderTest {

	/**
	 * Tests nextData on an empty stream (return null)
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void hasNextDataEmptyStreamTest() throws ReadFromStreamException {
		Assert.assertNull(getDecorator("DataEmpty.xml").readData());
	}

	/**
	 * Tests readData on a stream with one item: readData returns
	 * item, readData returns null, twice
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void hasNextDataWithOneItemStreamTest() throws ReadFromStreamException {
		DataReader decorator = getDecorator("DataOneItem.xml");
		testItemFully(123456781L, "1231.567890", decorator);
		Assert.assertNull(decorator.readData());
		Assert.assertNull(decorator.readData());
	}

	/**
	 * Tests a stream with several items, making sure the iterator goes through
	 * each item, in the correct order
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test
	public void nextDataWithSeveralItemStreamTest() throws ReadFromStreamException {
		DataReader decorator = getDecorator("Data1.xml");
		testItemFully(123456781L, "1231.567890", decorator);
		testItemFully(123456784L, "1234.567890", decorator);
		testItemFully(123456787L, "1237.567890", decorator);
		Assert.assertNull(decorator.readData());
	}
	
	/**
	 * Tests a stream with invalid xml 
	 * 
	 * @throws ReadFromStreamException
	 */
	@Test(expected=org.dm.streamcombiner.reader.exception.ReadFromStreamException.class)
	public void invalidDataTest() throws ReadFromStreamException {
		DataReader decorator = getDecorator("Invalid.xml");
		Data data1 = decorator.readData();

	}

	/**
	 * Tests nextData (compare result with
	 * expected values)
	 * 
	 * @param expectedTimestamp
	 * @param expectedAmount
	 * @param decorator
	 * @throws ReadFromStreamException
	 */
	private void testItemFully(long expectedTimestamp, String expectedAmount, DataReader decorator)
			throws ReadFromStreamException {
		Data data1 = decorator.readData();
		Assert.assertEquals(expectedAmount, data1.getAmount());
		Assert.assertEquals(expectedTimestamp, data1.getTimestamp().longValue());
	}

	public abstract DataReader getDecorator(String file) throws ReadFromStreamException;

}
