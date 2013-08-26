package com.malaz.app;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class SpeachRecognitionActivity extends Activity {

	private static final int REQUEST_RECOGNIZE = 100;
	private TextView resultTextView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_speach_recognition, menu);
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resultTextView = new TextView(this);
		setContentView(resultTextView);
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getString(R.string.voice_ask));
		
		try {
			startActivityForResult(intent, REQUEST_RECOGNIZE);
		} catch (ActivityNotFoundException e) {
			// If no recognizer exists, download one from Google Play
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Not Available");
			builder.setMessage("There is no recognition application installed."
					+ " Would you like to download one?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Download, for example, Google Voice Search
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);
							marketIntent.setData(Uri.parse("market://details?id=com.google.android.voicesearch"));
						}
					});
			builder.setNegativeButton("No", null);
			builder.create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_RECOGNIZE && resultCode == Activity.RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			StringBuilder sb = new StringBuilder();
			for (String piece : matches) {
				sb.append(piece);
				sb.append('\n');
			}
			resultTextView.setText(sb.toString());
		} else {
			Toast.makeText(this, "Operation Canceled", Toast.LENGTH_SHORT).show();
		}
	}
}
