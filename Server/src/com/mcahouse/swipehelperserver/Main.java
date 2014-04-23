package com.mcahouse.swipehelperserver;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main extends Container implements ActionListener, Runnable {

	private static Robot robot;
	private static JFrame frame;
	private static ArrayList<JButton> buttons;
	private static String[] buttonNames = { "numLock", "windows", "alt-tab",
			"collapse", "files", "print screen" };
	private static JPanel buttonPanel;
	private static int port = 8085;//HARDCODED... change later?
	private static Server server;
	private static Boolean hitAlt;

	/**
	 * Constructor
	 */
	public Main() {
		setLayout(new FlowLayout());
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("Robot init failed:");
			e.printStackTrace();
		}
		buttonPanel = new JPanel();
		buttons = new ArrayList<JButton>();
		for (String bn : buttonNames)
			buttons.add(new JButton(bn));// creates array of buttons
		for (JButton jb : buttons) {
			jb.addActionListener(this);
			add(jb);// adds buttons to jpanel
		}
		add(buttonPanel);// adds buttons on panel to container

		hitAlt = false;
		
		try {
			System.out.println("SERVERIP: "+ InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		//note, putting this here causes the program to wait until there is a connection before anything happens!
		//perhaps place in a thread?
		try {
			server = new Server(port);
		} catch (IOException e) {
			System.out.println("Failed to create Server.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Could not start Server","Error",  JOptionPane.ERROR_MESSAGE);
		}
		
		(new Thread(this)).start();//start thread to listen to commands on phone

		System.out.println("Init complete");
	}

	/**
	 * Creates JFrame
	 */
	private static void createAndShowGUI() {
		frame = new JFrame("Swipe Helper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Main());
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Runs thread for JFrame
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		/*
		 * try {
		 * 
		 * callWindows(); callNumLock();
		 * 
		 * 
		 * test(); System.out.println("hi"); } catch (AWTException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	/**
	 * Simulates hitting window key
	 */
	private static void windows() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_WINDOWS);

	}

	/**
	 * Simulates hitting num lock
	 */
	private static void numLock() {
		robot.keyPress(KeyEvent.VK_NUM_LOCK);
		robot.keyRelease(KeyEvent.VK_NUM_LOCK);

	}

	//ALT-TAB sequence for changing windows.  note that user is in effect making the delay
	/**
	 * Simulates hitting alt
	 */
	private static void hitAlt()	{
		robot.keyPress(KeyEvent.VK_ALT);
		hitAlt = true;
	}
	
	/**
	 * Simulates releasing alt
	 */
	private static void releaseAlt()	{
		robot.keyRelease(KeyEvent.VK_ALT);
		hitAlt = false;
	}

	/**
	 * Simulates hitting tab
	 */
	private static void hitTab()	{
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}
	
	/**
	 * Simulates hitting alt, then tab as if changing windows
	 * @deprecated
	 */
	private static void nextWindow() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.delay(500);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.delay(500);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.delay(500);

	}

	/**
	 * Simulates hitting windows then D to collapse all windows
	 */
	private static void collapse() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
	}

	/**
	 * Win+E Opens file explorer
	 */
	private static void fileExplorer() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_E);
		robot.keyRelease(KeyEvent.VK_E);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
	}

	/**
	 * Printscreen Takes a picture of the screen and saves it in the clipboard
	 */
	private static void printScreen() {
		robot.keyPress(KeyEvent.VK_PRINTSCREEN);
	}

	/**
	 * Returns button in array with specified text
	 * 
	 * @param text
	 * @return button
	 */
	private JButton findButton(String text) {
		for (JButton jb : buttons) {
			if (jb.getText().equals(text)) {
				return jb;
			}
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {// CAUTION: make sure string
												// matches exactly the button's
												// text
		if (e.getSource() == findButton("numLock")) {
			numLock();
		} else if (e.getSource() == findButton("windows")) {
			windows();
		} else if (e.getSource() == findButton("alt-tab")) {
			nextWindow();
		} else if (e.getSource() == findButton("collapse")) {
			collapse();
		} else if (e.getSource() == findButton("files")) {
			fileExplorer();
		} else if (e.getSource() == findButton("print screen")) {
			printScreen();
		}
	}

	@Override
	public void run() {
		while(true)	{//check forever!
			String command = server.getCommand();
			System.out.println(hitAlt);
			if(command != null)	{
				if(command.contains("UP"))	{
					windows();//open up windows menu
				}
				else if(command.contains("DOWN"))	{
					collapse();//minimize all windows
				}
				else if(command.contains("RIGHT"))	{
					//alt-tab, next window
					if(!hitAlt)
					{
						hitAlt();
						hitTab();
					}
					else
						hitTab();
				}
				else if(command.contains("LEFT"))	{
					releaseAlt();
				}
				server.setCommand(null);
			}
		}
	}
}
