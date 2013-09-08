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
import com.malaz.util.LangUtil;

public class MainActivity extends BaseActivity {

	private final static int CHECKING_BALANCE = 1;
	
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
		Intent intent = new Intent(this, TransfereActivity.class);
		startActivity(intent);
	}
	
	public void currentButtonClicked(View view) {		
		SIMService service = new ServiceFactory(this).getCurrentBalanceService();
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(MainActivity.this);
			return;
		}
		
		CallUtil.current(this, service, CHECKING_BALANCE);
	}
	
	public void aboutButtonClicked(View view) {
		Intent intent = new Intent(this, ReportsMainActivity.class);
		startActivity(intent);
	}
	
	public void callMeButtonClicked(View view) {
		Intent intent = new Intent(this, CallmeActivity.class);
		startActivity(intent);
	}
	
	public void settingsButtonClicked(View view) {
		Intent i = new Intent("com.malaz.app.ApplicationPreferenceActivity");
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data) {
		if (  resultCode == RESULT_OK ) {
			Toast.makeText(this,  getString(R.string.toast_main_check_balance), Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, getString(R.string.toast_main_check_balance_error) + resultCode, Toast.LENGTH_LONG).show();
		}		
	}
}
