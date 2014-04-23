package com.mcahouse.swipehelper;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;

public class GestureTestActivity extends Activity implements 
		GestureDetector.OnGestureListener,
		GestureDetector.OnDoubleTapListener {

	private GestureDetectorCompat mDetectorCompat;
	private TextView outputTextView;
	private Client client;//well, this program is the client...
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_test);
		
		outputTextView = (TextView) findViewById(R.id.textviewOutput);
		
		mDetectorCompat = new GestureDetectorCompat(this, this);
		mDetectorCompat.setOnDoubleTapListener(this);
		
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
					String host = "192.168.0.106";//hard coded to Roland's computer
					//TODO figure out how to find the host computer (otherwise run the server on the web or something)
					int port = 8085;//HARDCODED...change later?
		        	client = new Client(host, port);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture_test, menu);
		
		//strict mode - prevents a "networkonmain" exception
		//meh... it didn't work
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().penaltyDeath().build());
		
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		
		return true;
	}
	
	private void updateText(String input) {
		outputTextView.setText(input);
		client.writeToPC(input);//send it to the server
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
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
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

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		updateText("SINGLE TAP");
		return true;
	}

}
