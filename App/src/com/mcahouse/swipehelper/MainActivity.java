package com.mcahouse.swipehelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static String ipAddress; 
	
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
				final Intent gestureActivity = new Intent(getApplicationContext(), GestureTestActivity.class);
				final EditText ipInput = new EditText(MainActivity.this);
				new AlertDialog.Builder(MainActivity.this).setTitle("Enter IP")
				.setMessage("Enter computer IP")
				.setView(ipInput)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ipAddress = ipInput.getText().toString();

						startActivity(gestureActivity);
					}
				}
				).show();
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
