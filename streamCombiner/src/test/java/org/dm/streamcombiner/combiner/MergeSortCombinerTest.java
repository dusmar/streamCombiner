package org.dm.streamcombiner.combiner;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

public class MergeSortCombinerTest {

	Combiner comb = new MergeSortCombiner();

	/**
	 * Tests combine algorithm for 3 simple input streams (each contains 3
	 * items, no merging of amount is needed)
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	@Test
	public void combine3InputsWithoutAmountMergeTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();

		String expectedResult = readFile("Data1Data2Data3.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data1.xml"),
				classLoader.getResourceAsStream("Data2.xml"), classLoader.getResourceAsStream("Data3.xml")}; 
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 3 simple input streams with 6 items. All
	 * input streams are same
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	@Test
	public void combine6SameInputsTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data4Data5Data6.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data4.xml"),
				classLoader.getResourceAsStream("Data5.xml"), classLoader.getResourceAsStream("Data6.xml")}; 
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 1 simple input
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	@Test
	public void combine1InputTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data1.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data1.xml") }; 
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 4 simple input streams (7, 8, 9, 10) with 3,4,
	 * 6,7 items. Merging needed. 
	 * 	Merged are 
	 * 		2, 3 item from streams 7,8.
	 *      4, 5, 6 item from streams 8, 9, 10  
	 *      6, 7 item from stream 9, 10
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	@Test
	public void combine4InputsWithMergingTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data7Data8Data9Data10.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data7.xml"),
				classLoader.getResourceAsStream("Data8.xml"), classLoader.getResourceAsStream("Data9.xml"),
				classLoader.getResourceAsStream("Data10.xml")}; 
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}
	
	public void close(InputStream[] inputs, OutputStream output) throws IOException{
		for (InputStream input : inputs) {
			input.close();
		}
		output.close();
		
	}

	private static String readFile(String file) throws IOException {
		ClassLoader classLoader = MergeSortCombinerTest.class.getClassLoader();

		BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(file)));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

}