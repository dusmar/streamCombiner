package org.dm.streamcombiner.combiner.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dm.streamcombiner.combiner.Combiner;
import org.junit.Assert;
import org.junit.Test;

public class MergeSortCombinerImplTest {

	Combiner comb = new MergeSortCombinerImpl();

	/**
	 * Tests combine algorithm for 3 simple input streams  (each contains 3 items, no merging of amount is needed)
	 * @throws IOException
	 */
	@Test
	public void combine3InputsWithoutAmountMergeTest() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		String expectedResult = readFile("Data1Data2Data3.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		comb.combine(
				new InputStream[] { classLoader.getResourceAsStream("Data1.xml"),
						classLoader.getResourceAsStream("Data2.xml"), classLoader.getResourceAsStream("Data3.xml") },
				output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 3 simple input streams  (each contains 3 items, merging of amounts needed - for 2 items and also for 3 items)
	 * @throws IOException
	 */
	@Test
	public void combine3InputsWithAmountMergeTest() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		String expectedResult = readFile("Data4Data5Data6.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		comb.combine(
				new InputStream[] { classLoader.getResourceAsStream("Data4.xml"),
						classLoader.getResourceAsStream("Data5.xml"), classLoader.getResourceAsStream("Data6.xml") },
				output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	
	
	private static String readFile(String file) throws IOException {
		ClassLoader classLoader = MergeSortCombinerImplTest.class.getClass().getClassLoader();

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