package com.mcahouse.swipehelperserver;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main extends Container implements ActionListener, Runnable, KeyEventDispatcher {

	private static Robot robot;
	private static JFrame frame;
	private static ArrayList<JButton> buttons;
	private static String[] buttonNames = { "numLock", "windows", "alt-tab",
			"collapse", "files", "print screen" };
	private static String[] keys = {"Enter", "Windows", "Alt", "Tab", "Up", "Down", "Left", "Right", "F4"};
	private static Boolean windowsMenuUp;
	private static Boolean altTabUp;
	private static HashMap<String, Boolean> keyMap;//holds state of keys, true = pressed
	private static JPanel buttonPanel;
	private static int port = 8085;//HARDCODED... change later?
	private static Server server;
	private static JLabel ipLabel;//shows the ipaddress that user will input
	
	private static JButton startButton;
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
		
		startButton = new JButton("Start");
		
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayActionsScreen();				
			}
			
		});
		
		keyMap = new HashMap<String, Boolean>();
		//init hashmap of keys
		for(String key : keys)	{
			keyMap.put(key, false);//note, put also used for updating keys too
		}
		windowsMenuUp = false;//note that windows menu pops up upon RELEASE of windows key!!!
		/*listen on keystates in order to ensure that if user
		does something on computer this program will know*/
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
		
		altTabUp = false;//Alt tab menu is not active
		
		addToTray();//add icon to system tray for user's convenience
		
		try {
			System.out.println("SERVERIP: "+ InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		//note, putting this here causes the program to wait until there is a connection before anything happens!
		//perhaps place in a thread?
		
		try {
			ipLabel = new JLabel("Enter the numbers into your device: " + InetAddress.getLocalHost().toString().split("/")[1]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		add(ipLabel);
		add(startButton);
		
		
		System.out.println("Init complete");
		
	
	}
	
	private void displayActionsScreen(){
		this.remove(startButton);
		try {
			server = new Server(port);
		} catch (IOException e) {
			System.out.println("Failed to create Server.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Could not start Server","Error",  JOptionPane.ERROR_MESSAGE);
		}
		
		(new Thread(this)).start();//start thread to listen to commands on phone
		
		try {
			ipLabel.setText(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO, final version of this get rid of button panel
		buttonPanel = new JPanel();
		buttons = new ArrayList<JButton>();
		for (String bn : buttonNames)
			buttons.add(new JButton(bn));// creates array of buttons
		for (JButton jb : buttons) {
			jb.addActionListener(this);
			add(jb);// adds buttons to jpanel
		}
		add(buttonPanel);// adds buttons on panel to container
		this.revalidate();
		this.repaint();
	}
	
	private static void addToTray()	{
		//add to system tray
		if(!SystemTray.isSupported())	{
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		if(Main.class.getResource("SwipeIcon.gif") == null)	{
			System.out.println("Cannot find tray icon image");
		}
		final TrayIcon trayIcon = new TrayIcon(new ImageIcon(Main.class.getResource("SwipeIcon.gif")).getImage());
		final SystemTray tray = SystemTray.getSystemTray();
		
		//create menu items in pop up and add them to popup
		
		try	{
			tray.add(trayIcon);
		} catch(AWTException e)	{
			System.out.println("Trayicon could not be added");
			e.printStackTrace();
		}
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

	}

	/**
	 * Simulates hitting window key
	 */
	private static void windows() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
		windowsMenuUp = !windowsMenuUp;

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
	}
	
	/**
	 * Simulates releasing alt
	 */
	private static void releaseAlt()	{
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	/**
	 * Simulates hitting tab
	 */
	private static void hitTab()	{
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}
	
	/**
	 * Simulates hitting left arrow key
	 */
	private static void hitLeftArrowKey()	{
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}
	
	/**
	 * Simulates hitting right arrow key
	 */
	private static void hitRightArrowKey()	{
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}
	
	/**
	 * Simulates hitting up arrow key
	 */
	private static void hitUpArrowKey()	{
		robot.keyPress(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_UP);
	}
	
	/**
	 * Simulates hitting down arrow key
	 */
	private static void hitDownArrowKey()	{
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
	}
	
	/**
	 * Simulates hitting enter key
	 */
	private static void hitEnter()	{
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	
	/**
	 * Simulates hitting F4, as in alt-f4 to close applications
	 */
	private static void hitF4()	{
		robot.keyPress(KeyEvent.VK_F4);
		robot.keyRelease(KeyEvent.VK_F4);
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
	 * Calls the alt+tab menu forth
	 */
	private static void switchWindow(){
		
		
		try {
			ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "alt_tab.lnk");
			Process process = pb.start();
			altTabUp = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			switchWindow();
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
			//System.out.println(keyMap.get("Alt"));
			System.out.println("WINDMENUUP: "+windowsMenuUp);

			if(command != null)	{
				if(command.contains("UP"))	{
					altTabUp = false;
					windows();//open up windows menu
					
				}
				else if(command.contains("DOWN"))	{
					/*if(windowsMenuUp)	{
						hitDownArrowKey();
					}
					else if(keyMap.get("Alt"))//if alt is held, let go
						releaseAlt();
					else	{
						collapse();//minimize all windows
					}*/
					collapse();
				}
				else if(command.contains("RIGHT"))	{
					
					/*
					if(windowsMenuUp)	{//i.e. close the start menu
						windows();
					}
					//alt-tab, next window
					else if(!keyMap.get("Alt"))
					{
						hitAlt();
						hitTab();
					}
					else
					{
						System.out.println("tab hit");
						hitTab();
					}*/
					if(!altTabUp)
						switchWindow();
					else
						hitRightArrowKey();
					
				}
				else if(command.contains("LEFT"))	{
					/*if(windowsMenuUp)	{//i.e. close the start menu
						windows();
					}
					if(!keyMap.get("Alt"))	{
						hitAlt();
						hitTab();
					}
					else
						hitLeftArrowKey();*/
					if(!altTabUp)
						switchWindow();
					else
						hitLeftArrowKey();
				}
				else if(command.contains("SINGLE TAP"))	{
					hitEnter();
					windowsMenuUp = false;
					altTabUp = false;
				}
				else if(command.contains("DOUBLE TAP"))	{
					//close the current application...
					hitAlt();
					hitF4();

					releaseAlt();
				}
				server.setCommand(null);
			}
		}
	}

	/**
	 * Listens on key states for: Enter, Windows, Alt, Tab, Up, Down, Left, Right
	 * @param e
	 * @return
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		synchronized(Main.class)	{
			switch(e.getID())	{
			case KeyEvent.KEY_PRESSED:
				switch(e.getKeyCode())	{
				case KeyEvent.VK_ENTER:
					keyMap.put("Enter", true);
				case KeyEvent.VK_WINDOWS:
					keyMap.put("Windows", true);
				case KeyEvent.VK_ALT:
					keyMap.put("Alt", true);
				case KeyEvent.VK_TAB:
					keyMap.put("Tab", true);
				case KeyEvent.VK_UP:
					keyMap.put("Up", true);
				case KeyEvent.VK_DOWN:
					keyMap.put("Down", true);
				case KeyEvent.VK_LEFT:
					keyMap.put("Left", true);
				case KeyEvent.VK_RIGHT:
					keyMap.put("Right", true);
				case KeyEvent.VK_F4:
					keyMap.put("F4", true);
				}
				break;
			case KeyEvent.KEY_RELEASED:
				switch(e.getKeyCode())	{
				case KeyEvent.VK_ENTER:
					keyMap.put("Enter", false);
				case KeyEvent.VK_WINDOWS:
					keyMap.put("Windows", false);
				case KeyEvent.VK_ALT:
					keyMap.put("Alt", false);
				case KeyEvent.VK_TAB:
					keyMap.put("Tab", false);
				case KeyEvent.VK_UP:
					keyMap.put("Up", false);
				case KeyEvent.VK_DOWN:
					keyMap.put("Down", false);
				case KeyEvent.VK_LEFT:
					keyMap.put("Left", false);
				case KeyEvent.VK_RIGHT:
					keyMap.put("Right", false);
				case KeyEvent.VK_F4:
					keyMap.put("F4", true);
				}
			}
		}
		return false;
	}
}
