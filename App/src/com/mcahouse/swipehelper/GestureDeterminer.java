package com.mcahouse.swipehelper;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

public class GestureDeterminer {
	
	public static String determineDirection(MotionEvent e1, MotionEvent e2) {	
		float deltaX = e2.getRawX() - e1.getRawX();
		float deltaY = e2.getRawY() - e1.getRawY();
		
		if (5 > Math.abs(deltaY) && 5 > Math.abs(deltaX)) {
			return "TAP";
		} else if (deltaX > 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			return ("RIGHT: " + String.valueOf(deltaX));
		} else if (deltaX < 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			return ("LEFT: " + String.valueOf(deltaX));
		} else if (deltaY < 0) {
			return ("UP: " + String.valueOf(deltaY));
		} else if (deltaY > 0) {
			return ("DOWN: " + String.valueOf(deltaY));
		} else {
			return "NONE";
		}
	}
}
