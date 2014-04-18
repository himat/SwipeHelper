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

public class Main extends Container implements ActionListener {

	private static Robot robot;
	private static JFrame frame;
	private static ArrayList<JButton> buttons;
	private static String[] buttonNames = { "numLock", "windows", "alt-tab",
			"collapse", "files", "print screen" };
	private static JPanel buttonPanel;
	private static int port = 8085;//HARDCODED... change later?
	private static Server server;

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

	/**
	 * Simulates hitting alt, then tab as if changing windows
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
}
