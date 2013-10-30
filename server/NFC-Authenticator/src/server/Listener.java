package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Listener implements Runnable {

	private int port;
	public boolean stop = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public Listener (int port) {
		this.port = port;
	}
	
	public void stop () {
		
		this.stop = true;
		try {
			serverSocket.close();
			if (clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void run () {
		try {
			serverSocket = new ServerSocket(port);
			clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                    true); 
			BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
	        { 
	         System.out.println ("Server: " + inputLine); 
	         out.println(inputLine); 

	         if (inputLine.equals("Bye.")) 
	             break; 
	        } 
			while (!stop) {
				// accept
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Socket closed");
			// e.printStackTrace();
		}
		
	}
	
}