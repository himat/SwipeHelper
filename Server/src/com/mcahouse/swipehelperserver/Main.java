package com.mcahouse.swipehelperserver;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends Container implements ActionListener {
	
	private static Robot robot;
	private static JFrame frame;
	private static ArrayList<JButton> buttons;
	private static String[] buttonNames = {"numLock", "windows", "alt-tab"};
	private static JPanel buttonPanel;
	
	/**
	 * Constructor
	 */
	public Main()	{
		setLayout(new FlowLayout());
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("Robot init failed:");
			e.printStackTrace();
		}
		buttonPanel = new JPanel();
		buttons = new ArrayList<JButton>();
		for(String bn : buttonNames)
			buttons.add(new JButton(bn));//creates array of buttons
		for(JButton jb : buttons)	{
			jb.addActionListener(this);
			add(jb);//adds buttons to jpanel
		}
		add(buttonPanel);//adds buttons on panel to container
		
		System.out.println("Init complete");
	}
	
	/**
	 * Creates JFrame
	 */
	private static void createAndShowGUI()	{
		frame = new JFrame("Swipe Helper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Main());
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Runs thread for JFrame
	 * @param args
	 */
	public static void main(String[] args){
		 
		SwingUtilities.invokeLater(new Runnable()	{
			public void run()	{
				createAndShowGUI();
			}
		});
		
		/*try {
			 
			callWindows();
			callNumLock();
			
			
	         test();
	        System.out.println("hi");
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
         
	}
	
	/**
	 * Simulates hitting window key
	 */
	private static void callWindows(){
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_WINDOWS); 
		
	}
	
	/**
	 * Simulates hitting num lock
	 */
	private static void callNumLock(){
		robot.keyPress(KeyEvent.VK_NUM_LOCK);
		robot.keyRelease(KeyEvent.VK_NUM_LOCK);
	
	}
	
	/**
	 * Simulates hitting alt, then tab as if changing windows
	 */
	private static void test(){
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
	 * Returns button in array with specified text
	 * @param text
	 * @return button
	 */
	private JButton findButton(String text)	{
		for(JButton jb : buttons)	{
			if(jb.getText().equals(text))	{
				return jb;
			}
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {//CAUTION: make sure string matches exactly the button's text
		if(e.getSource() == findButton("numLock"))	{
			callNumLock();
		}
		else if(e.getSource() == findButton("windows"))	{
			callWindows();
		}
		else if(e.getSource() == findButton("alt-tab"))	{
			test();
		}
	}
}
