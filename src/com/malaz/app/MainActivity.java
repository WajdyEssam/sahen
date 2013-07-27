package com.malaz.app;

import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void chargeButtonClicked(View view) {
		Intent intent = new Intent(this, ChargeActivity.class);
		startActivity(intent);
	}

	public void convertButtonClicked(View view) {
		Intent intent = new Intent(this, ConvertActivity.class);
		startActivity(intent);
	}
	
	public void currentButtonClicked(View view) {		
		SIMService service = new ServiceFactory(this).getCurrentBalanceService();
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(MainActivity.this);
			return;
		}
		
		boolean state = CallUtil.current(this, service);
		
		if ( state ) {
			Toast.makeText(this, "Checking Balance Done!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Error in Checking Balance", Toast.LENGTH_LONG).show();
		}
	}
	
	public void aboutButtonClicked(View view) {
		Toast.makeText(this, "Malaz Mustafa, Android Developer (malazwajdy@hotmail.com)", Toast.LENGTH_LONG).show();
		
		SharedPreferences appPrefs = getSharedPreferences("sahen_sudani", MODE_PRIVATE);
		DisplayText(appPrefs.getString("companyName", ""));
		DisplayText(appPrefs.getString("simNo", ""));
	}
	
	public void callMeButtonClicked(View view) {
		Intent intent = new Intent(this, CallmeActivity.class);
		startActivity(intent);
	}
	
	public void settingsButtonClicked(View view) {
		Intent i = new Intent("com.malaz.app..ApplicationPreferenceActivity");
		startActivity(i);
	}
	
	private void DisplayText(String string) {
		Toast.makeText(getBaseContext(), string, Toast.LENGTH_LONG).show();
	}
}
