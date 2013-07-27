package com.malaz.app;

import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CallmeActivity extends Activity {

	private EditText numberEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callme);
		
		numberEditText = (EditText) findViewById(R.id.otherNumberEditText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_callme, menu);
		return true;
	}

	public void sendCallmeButtonClicked(View view) {
		String number = numberEditText.getText().toString();
		
		SIMService service = new ServiceFactory(this).getCallMeService(number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(CallmeActivity.this);
			return;
		}
		
		boolean state = CallUtil.callme(this, service);

		if ( state ) {
			Toast.makeText(this, "Callme is sending successfully!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Error in sending call me message", Toast.LENGTH_LONG).show();
		}
	}
}
