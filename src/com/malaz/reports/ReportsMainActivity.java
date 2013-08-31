package com.malaz.reports;

import com.malaz.app.BaseActivity;
import com.malaz.app.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ReportsMainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_reports);
		
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(getString(R.string.tab_weekly),
						getResources().getDrawable(R.drawable.home))
				.setContent(new Intent(this, WeeklyReportActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(getString(R.string.tab_monlty),
						getResources().getDrawable(R.drawable.invoice))
				.setContent(new Intent(this, MonthlyReportActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(getString(R.string.tab_yearly),
						getResources().getDrawable(R.drawable.products))
				.setContent(new Intent(this, YearlyReportActivity.class)));
		
		tabHost.setCurrentTab(0);
	}
}