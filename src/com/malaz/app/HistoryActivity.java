package com.malaz.app;

import com.malaz.database.HistoryDB;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends Activity {

	private HistoryDB database;
	private SimpleCursorAdapter cursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		//this.database.addTestData();
		
		displayListView();
		setTheTitle(actionBar, (int)this.database.getNumberOfHistories());
	}

	private void displayListView() {
		Cursor cursor = this.database.getHistoriesCursor();
		
		if ( cursor == null ) 
			return;
		
		String[] columns = new String[] {
				HistoryDB.ID,
				HistoryDB.OPERATION_ID,
				HistoryDB.OPEARTION_TIME
		};
		
		int[] to = new int[] {
			R.id.type,
			R.id.desc,
			R.id.time
		};
		
		this.cursorAdapter = new SimpleCursorAdapter(this, R.layout.history_list , cursor, columns, to, 0);
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(cursorAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			   String countryCode = cursor.getString(cursor.getColumnIndexOrThrow(HistoryDB.OPEARTION_AMOUNT));
			   Toast.makeText(getApplicationContext(),countryCode, Toast.LENGTH_SHORT).show();		 
		   }
		});
	}
	
	private void setTheTitle(ActionBar bar, int count) {
		bar.setTitle("Application Logs " + "(" + count + ")");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_history, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (android.R.id.home):
			this.finish();
			return true;
			
		case R.id.remove:
			this.database.clearHistories();	
			this.cursorAdapter.notifyDataSetChanged();
			Toast.makeText(this, "Clear All Logs", Toast.LENGTH_SHORT).show();
			setTheTitle(getActionBar(), (int)this.database.getNumberOfHistories());
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
