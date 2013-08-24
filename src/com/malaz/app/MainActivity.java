package com.malaz.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.malaz.reports.ReportsMainActivity;
import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;
import com.malaz.util.Constants;
import com.malaz.util.LangUtil;
import com.malaz.util.Preferences;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_main);		
		this.initializingActionBar();
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
		Intent intent = new Intent(this, ReportsMainActivity.class);
		startActivity(intent);
		
//		Toast.makeText(this, "Malaz Mustafa, Android Developer (malazwajdy@hotmail.com)", Toast.LENGTH_LONG).show();
//		
//		DisplayText(Preferences.getValue(this, Constants.COMPANY_NAME, ""));
//		DisplayText(Preferences.getValue(this, Constants.SIM_NUMBER, ""));
	}
	
	public void callMeButtonClicked(View view) {
		Intent intent = new Intent(this, CallmeActivity.class);
		startActivity(intent);
	}
	
	public void settingsButtonClicked(View view) {
		Intent i = new Intent("com.malaz.app.ApplicationPreferenceActivity");
		startActivity(i);
	}
	
	private void DisplayText(String string) {
		Toast.makeText(getBaseContext(), string, Toast.LENGTH_LONG).show();
	}
}
