package org.dm.streamcombiner.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SingleThreadedServer implements Runnable {

	protected int serverPort = 8080;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	private String response = null;

	public SingleThreadedServer(int port, String response) throws IOException {
		this.serverPort = port;
		this.response = response;
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();

		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			try {
				processClientRequest(clientSocket);
			} catch (IOException e) {
				// log exception and go on to next request.
			}
		}

		System.out.println("Server Stopped.");
	}

	private void processClientRequest(Socket clientSocket) throws IOException {
		OutputStream output = clientSocket.getOutputStream();
		InputStream input = clientSocket.getInputStream();

		output.write(response.getBytes());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}
}