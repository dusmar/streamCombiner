package org.dm.streamcombiner.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.dm.streamcombiner.combiner.Combiner;
import org.dm.streamcombiner.combiner.CombinerFactory;
import org.dm.streamcombiner.reader.exception.ReadFromStreamException;

/**
 * 
 * @author Dusan Maruscak
 *
 */
public class Main {

	private static Properties configuration;
	public static int DEFAULT_TIMEOUT = 2000;
	private static int timeout = DEFAULT_TIMEOUT;
	public static final String CONF_FILE = "config.properties";
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/**
	 * Create socket to connect to server. Timeout is specified CONF_FILE file
	 * to prevent from stream hanging.
	 * 
	 * @param host
	 *            in format server:port
	 * @return InputStream to read data from server
	 * @throws IOException
	 */
	public static InputStream stringToStream(String host) {
		String[] hostAndPort = host.split(":");
		if (hostAndPort.length != 2) {
			LOGGER.warning(String.format("'%s' not in expected format host:port. Will be ignored ", host));
			return null;
		}
		String hostName = hostAndPort[0];
		int portNumber = 0;
		try {
			portNumber = Integer.parseInt(hostAndPort[1]);
		} catch (NumberFormatException e) {
			LOGGER.warning(String.format("Cannot parse host for %s. Will be ignored ", host));
			return null;

		}

		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(hostName, portNumber), timeout);
			socket.setSoTimeout(timeout);
			InputStream input = socket.getInputStream();
			return input;
		} catch (IOException ex) {
			LOGGER.warning(String.format("Can't connect  to %s. Skipped", host));
		}
		return null;

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

	/**
	 * Prepares array of InputStreams based on configuration
	 * 
	 * @return
	 */
	public static InputStream[] getInputStreams() {
		String hosts = configuration.getProperty("hosts");
		if (hosts == null || hosts.isEmpty()) {
			LOGGER.severe("Can't find host property");
			return null;
		}
		try {
			timeout = Integer.parseInt(configuration.getProperty("timeout"));
		} catch (NumberFormatException e) {
			LOGGER.warning("Cannot parse timeout property. Default one will be used");

		}

		String[] hostsArray = hosts.split(",");
		List<InputStream> inputs = new ArrayList<InputStream>();
		for (String host : hostsArray) {
			InputStream input = stringToStream(host);
			if (input != null) {
				inputs.add(input);
			}
		}
		return inputs.toArray(new InputStream[inputs.size()]);

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
		SingleThreadedServer server1, server2, server3;
		new Thread(server1 = new SingleThreadedServer(8080, readFile("Data1.xml"))).start();
		new Thread(server2 = new SingleThreadedServer(8081, readFile("Data2.xml"))).start();
		new Thread(server3 = new SingleThreadedServer(8082, readFile("Data3.xml"))).start();
		InputStream[] inputs = getInputStreams();
		if (inputs != null) {
			Combiner combiner = CombinerFactory.getCombiner();
			combiner.combine(inputs, System.out);
			for (InputStream input : inputs) {
				input.close();
			}
		}

		server1.stop();
		server2.stop();
		server3.stop();
	}

}
