package com.mcahouse.swipehelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.support.v4.view.GestureDetectorCompat;

public class GestureImageView extends ImageView implements
		GestureDetector.OnGestureListener,
		GestureDetector.OnDoubleTapListener {

	private GestureDetectorCompat mDetectorCompat;
	
	public GestureImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mDetectorCompat = new GestureDetectorCompat(context, this);
		mDetectorCompat.setOnDoubleTapListener(this);
	}

	public GestureImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mDetectorCompat = new GestureDetectorCompat(context, this);
		mDetectorCompat.setOnDoubleTapListener(this);
	}

	public GestureImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mDetectorCompat = new GestureDetectorCompat(context, this);
		mDetectorCompat.setOnDoubleTapListener(this);
	}

	private void updateText(String input) {
		//client.writeToPC(input);//send it to the server
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		this.mDetectorCompat.onTouchEvent(e);		
		return super.onTouchEvent(e);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		float deltaX = e2.getRawX() - e1.getRawX();
		float deltaY = e2.getRawY() - e1.getRawY();
		
		if (deltaX > 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			updateText("RIGHT: " + String.valueOf(deltaX));
		} else if (deltaX < 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
			updateText("LEFT: " + String.valueOf(deltaX));
		} else if (deltaY < 0) {
			updateText("UP: " + String.valueOf(deltaY));
		} else if (deltaY > 0) {
			updateText("DOWN: " + String.valueOf(deltaY));
		}
		
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		updateText("SINGLE TAP");
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		updateText("DOUBLE TAP");
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	
}