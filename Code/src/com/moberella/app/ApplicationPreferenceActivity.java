package com.moberella.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.moberella.app.R;
import com.moberella.database.HistoryDB;
import com.moberella.util.Constants;
import com.moberella.util.LangUtil;
import com.moberella.util.Preferences;

public class ApplicationPreferenceActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);

		// set file name
		getPreferenceManager().setSharedPreferencesName(Constants.FILE_NAME);

		// load preference from XML file
		addPreferencesFromResource(R.xml.settingsui);
		PreferenceManager.setDefaultValues(ApplicationPreferenceActivity.this, R.xml.settingsui, false);

		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initSummary(getPreferenceScreen().getPreference(i));
		}

		Preference button = (Preference) findPreference("clearButton");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {

				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							clearDB();
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationPreferenceActivity.this);
				builder.setMessage(getString(R.string.sure_message))
						.setPositiveButton(getString(R.string.yes_message), dialogClickListener)
						.setNegativeButton(getString(R.string.no_message), dialogClickListener).show();
				return true;
			}
		});

		Preference sharePref = findPreference("pref_share");
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this app!");
		shareIntent.putExtra(Intent.EXTRA_TEXT,"Check this awesome app at: http://sahen.moberella.com/");
		sharePref.setIntent(shareIntent);

		Preference ratePref = findPreference("pref_rate");
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		ratePref.setIntent(goToMarket);
	}

	private void clearDB() {
		final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.clearDb), getString(R.string.wait_message));
		new Thread(new Runnable() {
			@Override
			public void run() {
				HistoryDB.getInstance(ApplicationPreferenceActivity.this).clearHistories();
				dialog.dismiss();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ApplicationPreferenceActivity.this,
								getString(R.string.operation_done_sucesffully),
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updatePrefSummary(findPreference(key));

		if (key.equals(Constants.APPLICATION_LANGUAGE) ||
			key.equals(Constants.COMPANY_NAME)) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	private void initSummary(Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			for (int i = 0; i < pCat.getPreferenceCount(); i++) {
				initSummary(pCat.getPreference(i));
			}
		} else {
			updatePrefSummary(p);
		}
	}

	private void updatePrefSummary(Preference p) {
		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getEntry());
		}
		if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		}
	}
	
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
			preferecneIntent.addFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(preferecneIntent);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	} 
}
