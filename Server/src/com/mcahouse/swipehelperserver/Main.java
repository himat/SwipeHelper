package com.mcahouse.swipehelperserver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Main {
	static Robot robot;
	public static void main(String[] args){
		 
		try {
			robot = new Robot();
			 
			callWindows();
			callNumLock();
			
			
	         //test();
	        System.out.println("hi");
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
         
	}
	
	private static void callWindows(){
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_WINDOWS); 
		
	}
	
	private static void callNumLock(){
		robot.keyPress(KeyEvent.VK_NUM_LOCK);
		robot.keyRelease(KeyEvent.VK_NUM_LOCK);
	
	}
	
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
}
