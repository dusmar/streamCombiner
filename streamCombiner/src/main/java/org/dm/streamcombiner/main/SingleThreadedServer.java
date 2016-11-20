package org.dm.streamcombiner.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Used for test purposes to simulate host (server) which handles requests from
 * client (application) and returns data in expected format
 * 
 * @author Dusan Maruscak
 *
 */
public class SingleThreadedServer implements Runnable {

	protected int serverPort;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	private String response = null;

	public SingleThreadedServer(int port, String response) {
		this.serverPort = port;
		this.response = response;
	}

	public void run() {
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			try {
				handleClientRequest(clientSocket);
			} catch (IOException e) {
				// log exception and go on to next request.
			}
		}
	}

	private void handleClientRequest(Socket clientSocket) throws IOException {
		OutputStream output = clientSocket.getOutputStream();
		InputStream input = clientSocket.getInputStream();
		output.write(response.getBytes());
		output.close();
		input.close();
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Cannot open port %s", this.serverPort), e);
		}
	}
}