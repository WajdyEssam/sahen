package com.malaz.app;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
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

import com.malaz.adapters.HistoryAdapter;
import com.malaz.database.HistoryDB;
import com.malaz.model.History;
import com.malaz.util.LangUtil;
import com.malaz.util.Preferences;

public class HistoryActivity extends Activity {

	private HistoryDB database;
	private List<History> histories;
	
	@Override
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_history);
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		Resources resources = getResources();
		Drawable drawable = resources.getDrawable(R.drawable.header);
		actionBar.setBackgroundDrawable(drawable);
		
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		this.database = HistoryDB.getInstance(this);
		setTheTitle(actionBar, (int)this.database.getNumberOfHistories());
		
		displayListView();		
	}

	private void displayListView() {
		histories = this.database.getAllHistories();

		HistoryAdapter adapter = new HistoryAdapter(this, histories);	
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			   History history = histories.get(position);
			   Toast.makeText(getApplicationContext(), "Amount: " + Integer.toString(history.getAmount()), Toast.LENGTH_SHORT).show();		 
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
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
			
		case R.id.remove:
			this.database.clearHistories();				
			Toast.makeText(this,  R.string.clear_logs , Toast.LENGTH_SHORT).show();
			refreshList();
			setTheTitle(getActionBar(), (int)this.database.getNumberOfHistories());
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
	
	private void refreshList() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		final HistoryAdapter adapter = (HistoryAdapter) listView.getAdapter();

		if ( this.histories == null ) 
			return;
		
		this.histories.clear();
		
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	adapter.notifyDataSetChanged();
	        }
	    });
	}
}
