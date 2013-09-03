package com.malaz.app;

import com.malaz.util.AppUtil;
import com.malaz.util.Constants;
import com.malaz.util.Preferences;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity{
	
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
				" : " +  AppUtil.getSIMName(Preferences.getValue(this, Constants.COMPANY_NAME, ""), this));
		
	}

}