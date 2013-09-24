package com.moberella.reports;

import com.moberella.app.R;
import com.moberella.app.ApplicationPreferenceActivity;
import com.moberella.app.BaseActivity;
import com.moberella.app.LogsActivity;
import com.moberella.app.MainActivity;
import com.moberella.util.AppUtil;
import com.moberella.util.Constants;
import com.moberella.util.Preferences;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
		case android.R.id.home:
		case R.id.menu_home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
			
		case R.id.history:
			Intent intent2 = new Intent(this, LogsActivity.class);
			startActivity(intent2);
			break;			
			
		
		case R.id.menu_settings:
			Intent preferecneIntent = new Intent(this, ApplicationPreferenceActivity.class);
			startActivity(preferecneIntent);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
				" : " +  AppUtil.getSIMName(Preferences.getValue(this, Constants.COMPANY_NAME, ""), this));
		
	}
}