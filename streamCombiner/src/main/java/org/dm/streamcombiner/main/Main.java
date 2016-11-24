package org.dm.streamcombiner.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import org.dm.streamcombiner.combiner.Combiner;
import org.dm.streamcombiner.combiner.CombinerFactory;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class Main {

	private static int timeout = 2000;
	private static Properties configuration;
	public static final String CONF_FILE = "config.properties";

	/**
	 * Create socket to connect to server. Timeouts are set to 2000 ms to prevent from 
	 * stream hanging.
	 * 
	 * @param host
	 *            in format server:port
	 * @return InputStream to read data from server
	 * @throws IOException
	 */
	public static InputStream stringToStream(String host) throws IOException {
		String[] hostAndPort = host.split(":");
		String hostName = hostAndPort[0];
		int portNumber = Integer.parseInt(hostAndPort[1]);
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(hostName, portNumber), timeout);
		socket.setSoTimeout(timeout);
		return socket.getInputStream();
	}

	/**
	 * Read configurations from config.properties file
	 * 
	 * @throws IOException
	 */
	public static void initConfiguration() throws IOException {
		InputStream input = Main.class.getClassLoader().getResourceAsStream(CONF_FILE);
		configuration = new Properties();
		configuration.load(input);
	}

	/**
	 * Read content of file. Just just to create response of test servers.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String readFile(String file) throws IOException {
		ClassLoader classLoader = Main.class.getClassLoader();

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

	/**
	 * Prepares input, array of InputStream based on configuration
	 * 
	 * @return
	 * @throws IOException
	 */
	public static InputStream[] getInputStreams() throws IOException {
		String hosts = configuration.getProperty("hosts");
		if (hosts==null || hosts.isEmpty()) {
			return new InputStream[0];
		}
		String[] hostsArray = hosts.split(",");
		InputStream[] inputs = new InputStream[hostsArray.length];
		int i = 0;
		for (String host : hostsArray) {
			inputs[i++] = stringToStream(host);
		}
		return inputs;

	}

	/**
	 * Main method of Stream Combiner application. List of hosts is read from
	 * config.properties file
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ReadFromStreamException 
	 */
	public static void main(String[] args) throws IOException, ReadFromStreamException {
		initConfiguration();
		//SingleThreadedServer server1, server2, server3;
		//new Thread(server1 = new SingleThreadedServer(8080, readFile("Data1.xml"))).start();
		//new Thread(server2 = new SingleThreadedServer(8081, readFile("Data2.xml"))).start();
		//new Thread(server3 = new SingleThreadedServer(8082, readFile("Data3.xml"))).start();
		InputStream[] inputs = getInputStreams();
		Combiner combiner = CombinerFactory.getCombiner();
		combiner.combine(inputs, System.out);
		for (InputStream input : inputs) {
			input.close();
		}
		
		//server1.stop();
		//server2.stop();
		//server3.stop();
	}

}
