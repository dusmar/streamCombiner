package org.dm.streamcombiner.combiner;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.dm.streamcombiner.main.Main;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;
import org.junit.Assert;
import org.junit.Test;

public class MergeSortCombinerTest {

	Combiner comb = new MergeSortCombiner();

	/**
	 * Tests combine algorithm for 3 simple input streams (each contains 3
	 * items, no merging of amount is needed). Data Objects are nested. It means
	 * that Data Object from one stream goes to output, then data object from
	 * another one may go to output etc
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine3InputsWithoutAmountMergeNestedTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();

		String expectedResult = readFile("Data1Data2Data3.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data1.xml"),
				classLoader.getResourceAsStream("Data2.xml"), classLoader.getResourceAsStream("Data3.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 3 simple input streams (each contains 3
	 * items, no merging of amount is needed). Data are not nested. It means
	 * that all Data Objects from first stream go to output, then data from
	 * second stream etc
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine3InputsWithoutAmountMergeNoNestedTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();

		String expectedResult = readFile("Data11Data12Data13.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data11.xml"),
				classLoader.getResourceAsStream("Data12.xml"), classLoader.getResourceAsStream("Data13.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 3 simple input streams with 6 items. All
	 * input streams are same.
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
				classLoader.getResourceAsStream("Data5.xml"), classLoader.getResourceAsStream("Data6.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Tests combine algorithm for 1 simple input and 1 empty
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine1InputAnd1EmptyTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data1.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data1.xml"),
				classLoader.getResourceAsStream("DataEmpty.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}
	

	/**
	 * Tests combine algorithm for 2 empty streams
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine2EmptyInputsTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = "";
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("DataEmpty.xml"),
				classLoader.getResourceAsStream("DataEmpty.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	

	/**
	 * Tests combine algorithm for 2 streams: 1. valid, 2. invalid
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine1ValidInputAndOneInvalidNoExceptionTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data1.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data1.xml"),
				classLoader.getResourceAsStream("Data11Data12Data13.json") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}


	/**
	 * Tests combine algorithm for 2 streams: 1.invalid xml, 2. valid
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine1ValidInputAndOneInvalidExceptionTest() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data1.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Invalid.xml"),
				classLoader.getResourceAsStream("Data1.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}





	/**
	 * Tests combine algorithm for 4 simple input streams (7, 8, 9, 10) with
	 * 3,4, 6,7 items. Merging needed. Merged are 2, 3 item from streams 7,8. 4,
	 * 5, 6 item from streams 8, 9, 10 6, 7 item from stream 9, 10
	 * 
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
				classLoader.getResourceAsStream("Data10.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	/**
	 * Same as combine4InputsWithMergingTest, just order of input streams is
	 * changed (1. and 3. are swapped)
	 * 
	 * @throws IOException
	 * @throws ReadFromStreamException
	 */
	@Test
	public void combine4InputsWithMergingTest1() throws IOException, ReadFromStreamException {
		ClassLoader classLoader = getClass().getClassLoader();
		String expectedResult = readFile("Data7Data8Data9Data10.json");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream[] inputs = new InputStream[] { classLoader.getResourceAsStream("Data9.xml"),
				classLoader.getResourceAsStream("Data8.xml"), classLoader.getResourceAsStream("Data7.xml"),
				classLoader.getResourceAsStream("Data10.xml") };
		comb.combine(inputs, output);
		close(inputs, output);
		Assert.assertEquals(expectedResult, output.toString());
	}

	public void close(InputStream[] inputs, OutputStream output) throws IOException {
		for (InputStream input : inputs) {
			input.close();
		}
		output.close();

	}

	private static String readFile(String file) throws IOException {
		ClassLoader classLoader = Main.class.getClassLoader();
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(file)))) {

			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			return stringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}