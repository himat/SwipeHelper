package com.mcahouse.swipehelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;

public class GestureOverlayService extends Service {

	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	
	private RelativeLayout expandedView;
	private ImageView minimizedView;
	
	private ImageView expandedImageView;
	private Button expandedCloseButton;
	
	private GestureImageView gestureImageView;
	
	public Client client;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		
		expandedView = (RelativeLayout) ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.service_gesture_overlay, null);

		expandedCloseButton = (Button) expandedView.findViewById(R.id.overlayCloseButton);
		expandedImageView = (ImageView) expandedView.findViewById(R.id.overlayImageView);
		
		minimizedView = new ImageView(this);
		minimizedView.setImageResource(R.drawable.ic_launcher);
		
		expandedCloseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GestureOverlayService.this.stopSelf();
			}
		});
		
		expandedImageView.setOnTouchListener(new View.OnTouchListener() {		
			private long downTime;

			@Override public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downTime = System.currentTimeMillis();
					return true;
				case MotionEvent.ACTION_UP:
					windowManager.removeView(expandedView);
					windowManager.addView(minimizedView, params);						
					return true;
				}
				return false;
			}
		});
		
		minimizedView.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;
			
			private long downTime;

			@Override public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					downTime = System.currentTimeMillis();
					return true;
				case MotionEvent.ACTION_UP:
					long diff = System.currentTimeMillis() - downTime;
					if (200 > diff) {
						windowManager.removeView(minimizedView);
						windowManager.addView(expandedView, params);
					}
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX + (int) (event.getRawX() - initialTouchX);
					params.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(minimizedView, params);
					return true;
				}
				return false;
			}
		});

		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
					String host = MainActivity.ipAddress;//"192.168.4.103";//hard coded to Roland's computer
					//TODO figure out how to find the host computer (otherwise run the server on the web or something)
					int port = 8085;//HARDCODED...change later?
		        	client = new Client(host, port);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});
		thread.start();
		
		gestureImageView = (GestureImageView) expandedView.findViewById(R.id.gestureImageView);
		
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
					WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER;
		params.x = 0;
		params.y = 100;
		
		windowManager.addView(minimizedView, params);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (client != null) {
			try {
				client.closeConnection();
			} catch (Exception e) { }
		}		
		if (expandedView != null) {
			try {
				windowManager.removeView(expandedView);
			} catch (Exception e) { }
		} else if (minimizedView != null) {
			try {
				windowManager.removeView(minimizedView);
			} catch (Exception e) { }
		}
	}
}
