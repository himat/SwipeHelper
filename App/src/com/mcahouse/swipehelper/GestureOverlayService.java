package com.mcahouse.swipehelper;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
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

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

		//chatHead = new ImageView(this);
		//chatHead.setImageResource(R.drawable.ic_launcher);
		
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
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					windowManager.removeView(expandedView);
					windowManager.addView(minimizedView, params);
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX + (int) (event.getRawX() - initialTouchX);
					params.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(expandedView, params);
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

			@Override public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					windowManager.removeView(minimizedView);
					windowManager.addView(expandedView, params);
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
