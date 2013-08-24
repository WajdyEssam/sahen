package com.malaz.app;

import com.malaz.util.LangUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class YearlyReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		LangUtil.setLocale(this);		
		setContentView(R.layout.activity_yearly_report);
		//this.initializingActionBar();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_yearly_report, menu);
		return true;
	}

}
