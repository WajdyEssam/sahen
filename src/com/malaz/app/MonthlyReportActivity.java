package com.malaz.app;

import com.malaz.util.LangUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MonthlyReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LangUtil.setLocale(this);		
		setContentView(R.layout.activity_monthly_report);
		//this.initializingActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_monthly_report, menu);
		return true;
	}

}
