package com.mcahouse.swipehelperserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private ServerSocket ss;
	private DataOutputStream dout;//should be only 1...
	private Socket s;
	private String command;//current command received from phone
	private Boolean connected;
	private Thread mainThread;
	
	
	public Server(int port) throws IOException	{
		connected = false;
		listen(port);//wait for phone connection
	}
	
	private void listen(int port) throws IOException	{
		ss = new ServerSocket(port);//TODO make it find it's own ports then generate code to put in phone to find it's own port
		System.out.println("Listening on " + ss);
		//do we need a loop if it's only one connection?
		mainThread = new Thread(this);
		Thread wait = new Thread()	{
			public void run()	{
				try {
					s = ss.accept();
					System.out.println("Connection from "+s);
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					connected = true;
					mainThread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		wait.start();
	}
	
	public void removeConnection(Socket s)	{
		System.out.println("Removing connection to "+s);
		connected = false;
		try	{
			s.close();
		} catch(IOException ie)	{
			System.out.println("Error closing "+s);
			ie.printStackTrace();
		}
	}

	@Override
	public void run() {
		try	{
			DataInputStream din = new DataInputStream(s.getInputStream());
			
			while(true)	{
				String input = din.readUTF();
				System.out.println("GOT THIS: "+input);
				command = input;
				if(input.contains("EXIT"))
					break;//TODO code in phone to put a command called exit when it exits
				//do something with this input i.e. store it and give it to Main
			}
		} catch(EOFException ie)	{
			
		} catch(IOException ie)	{
			ie.printStackTrace();
		} finally	{
			removeConnection(s);
		}
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return true if connected
	 */
	public Boolean isConnected() {
		return connected;
	}
}
