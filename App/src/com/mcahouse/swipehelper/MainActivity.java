package com.mcahouse.swipehelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	public static String ipAddress; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button gestureTestButton = (Button) findViewById(R.id.startGestureActivity);
		gestureTestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				EditText ipInput = (EditText) findViewById(R.id.ip_box);
				ipAddress = ipInput.getText().toString();
				Intent launchService = new Intent(getApplicationContext(), GestureOverlayService.class);
				startService(launchService);				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
