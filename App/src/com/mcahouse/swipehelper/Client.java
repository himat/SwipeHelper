/**
 * 
 */
package com.mcahouse.swipehelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.util.Log;

/**
 * @author Roland
 *
 */
public class Client implements Runnable {

	private Socket socket;
	
	private DataOutputStream dout;
	private DataInputStream din;
	
	public Client(String host, int port)	{
		try	{
			socket = new Socket(host, port);
			Log.d("CONNECT", "connected to: " + socket);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			
			new Thread(this).start();
		} catch(IOException ie)	{
			Log.e("FAIL", "FAILED TO CONNECT boo hoo hoo");
			ie.printStackTrace();
		}
	}
	
	/**
	 * Test function to allow client to send to server an action
	 */
	public void writeToPC(String str)	{
		try	{
			dout.writeUTF(str);
		} catch(IOException ie)	{
			ie.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//not sure if the client needs to receive anything from server
	}
	
	public void closeConnection(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

