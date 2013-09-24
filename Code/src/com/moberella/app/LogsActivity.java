package com.moberella.app;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.moberella.app.R;
import com.moberella.adapters.LogsAdapter;
import com.moberella.database.HistoryDB;
import com.moberella.model.History;
import com.moberella.util.LangUtil;

public class LogsActivity extends Activity {

	private HistoryDB database;
	private List<History> histories;
	
	@Override
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_logs);
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		Resources resources = getResources();
		Drawable drawable = resources.getDrawable(R.drawable.header);
		actionBar.setBackgroundDrawable(drawable);
		
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		this.database = HistoryDB.getInstance(this);
		setTheTitle(actionBar, (int)this.database.getNumberOfLogs());
		
		displayListView();		
	}

	private void displayListView() {
		histories = this.database.getAllLogs();

		LogsAdapter adapter = new LogsAdapter(this, histories);	
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			   History history = histories.get(position);
			   Toast.makeText(getApplicationContext(), getString(R.string.toast_amount) + " " + Integer.toString(history.getAmount()), Toast.LENGTH_SHORT).show();		 
		   }
		});
	}
	
	private void setTheTitle(ActionBar bar, int count) {
		bar.setTitle(this.getResources().getString((R.string.application_logs)) + "(" + count + ")");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_history, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
			
		case R.id.remove:
			clearDB();
			return true;
			
		
		case R.id.menu_home:
			Intent intent2 = new Intent(this, MainActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
					

		case R.id.menu_settings:
			Intent preferecneIntent = new Intent(this, ApplicationPreferenceActivity.class);
			startActivity(preferecneIntent);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void clearDB() {
		final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.clear_logs), getString(R.string.wait_message));
		ListView listView = (ListView) findViewById(R.id.listView1);
		final LogsAdapter adapter = (LogsAdapter) listView.getAdapter();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				database.clearLogs();
				
				if ( histories == null ) 
					return;
				
				histories.clear();				
				dialog.dismiss();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						Toast.makeText(LogsActivity.this, getString(R.string.operation_done_sucesffully),Toast.LENGTH_SHORT).show();
						setTheTitle(getActionBar(), (int)database.getNumberOfLogs());
					}
				});
			}
		}).start();
	}
}
