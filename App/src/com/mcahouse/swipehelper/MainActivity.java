package com.mcahouse.swipehelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent launchService = new Intent(getApplicationContext(), GestureOverlayService.class);
		//startService(launchService); //Don't uncomment unless necessary
		
		Button gestureTestButton = (Button) findViewById(R.id.buttonGestureTest);
		gestureTestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent launchActivity = new Intent(getApplicationContext(), GestureTestActivity.class);
				
				startActivity(launchActivity);
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
