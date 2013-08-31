package com.malaz.reports;

import com.malaz.app.BaseActivity;
import com.malaz.app.HistoryActivity;
import com.malaz.app.MainActivity;
import com.malaz.app.R;
import com.malaz.util.AppUtil;
import com.malaz.util.Constants;
import com.malaz.util.Preferences;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class ReportsMainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_reports);
		initializingActionBar();
		
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
	
	// repeated code from base activity, until we found away to inhert this code
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_base, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (android.R.id.home):
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
			
		case R.id.history:
			Intent intent2 = new Intent(this, HistoryActivity.class);
			startActivity(intent2);
			break;			
		}
		
		return super.onOptionsItemSelected(item);
	} 
	
	protected void initializingActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		Resources resources = getResources();
		Drawable drawable = resources.getDrawable(R.drawable.header);
		actionBar.setBackgroundDrawable(drawable);
		
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(this.getResources().getString(R.string.app_name));
		actionBar.setSubtitle(this.getResources().getString(R.string.sim_name) + 
				" : " +  AppUtil.getSIMName(Preferences.getValue(this, Constants.COMPANY_NAME, "")));
		
	}
}