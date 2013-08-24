package com.malaz.reports;

import com.malaz.app.BaseActivity;
import com.malaz.app.MonthlyReportActivity;
import com.malaz.app.R;
import com.malaz.app.ReportActivity;
import com.malaz.app.YearlyReportActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ReportsMainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Weekly",
						getResources().getDrawable(R.drawable.home))
				.setContent(new Intent(this, ReportActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("Monthly",
						getResources().getDrawable(R.drawable.invoice))
				.setContent(new Intent(this, MonthlyReportActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("Yearly",
						getResources().getDrawable(R.drawable.products))
				.setContent(new Intent(this, YearlyReportActivity.class)));
		tabHost.setCurrentTab(0);
	}
}