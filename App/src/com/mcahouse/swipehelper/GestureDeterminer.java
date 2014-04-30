package com.mcahouse.swipehelper;

import android.util.Log;
import android.view.MotionEvent;

public class GestureDeterminer {
	
	public static String determineDirection(MotionEvent e1, MotionEvent e2, int pointerIndex) {	
		float deltaX;
		float deltaY;
		try {
			deltaX = e2.getX(pointerIndex) - e1.getX(pointerIndex);
			deltaY = e2.getY(pointerIndex) - e1.getY(pointerIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deltaX = e2.getX() - e1.getX();
			deltaY = e2.getY() - e1.getY();
		}
		
		if (5 > Math.abs(deltaY) && 5 > Math.abs(deltaX)) {
			return "SINGLE TAP";
		} else if (deltaX > 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			return ("RIGHT");
		} else if (deltaX < 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			return ("LEFT");
		} else if (deltaY < 0) {
			return ("UP");
		} else if (deltaY > 0) {
			return ("DOWN");
		} else {
			return "NONE";
		}
	}
	
	public static String determineMultiDirection(String s1, String s2) {
		if (s1.contentEquals(s2)) {
			if (s1.contentEquals("UP")) {
				return "TWO UP";
			} else if (s1.contentEquals("DOWN")) {
				return "TWO DOWN";
			} else if (s1.contentEquals("RIGHT")) {
				return "TWO RIGHT";
			} else if (s1.contentEquals("LEFT")) {
				return "TWO LEFT";
			} else {
				return "NONE";
			}
		} else {
			return "NONE";
		}
	}
}
