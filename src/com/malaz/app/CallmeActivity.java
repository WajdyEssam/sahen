package com.malaz.app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.malaz.database.Database;
import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;
import com.malaz.util.LangUtil;

public class CallmeActivity extends BaseActivity {

	private EditText numberEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_callme);
		this.initializingActionBar();
		
		numberEditText = (EditText) findViewById(R.id.otherNumberEditText);
	}

	public void sendCallmeButtonClicked(View view) {
		String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
		
		if ( number.isEmpty() ) {
			Toast.makeText(this, "Please Write Card Number Before Sending CallMe", Toast.LENGTH_LONG).show();
			return;
		}
		
		SIMService service = new ServiceFactory(this).getCallMeService(number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(CallmeActivity.this);
			return;
		}
		
		if ( service.getCallMeFormat().isEmpty() ) {
			Toast.makeText(this, "This Company Doesn't Support CallMe Service!", Toast.LENGTH_LONG).show();
			return;
		}
		
		boolean state = CallUtil.callme(this, service);

		if ( state ) {
			Database.saveSendingCallMe(this, number);
			Toast.makeText(this, "CallMe to " + number + " is sending successfully!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Error in sending call me message to" + number, Toast.LENGTH_LONG).show();
		}
	}
}
