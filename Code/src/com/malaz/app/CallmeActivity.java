package com.malaz.app;

import android.content.Intent;
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
	private final static int CALLME = 1;
	
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
			Toast.makeText(this, getString(R.string.toast_validate_callme_number), Toast.LENGTH_LONG).show();
			return;
		}
		
		SIMService service = new ServiceFactory(this).getCallMeService(number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(CallmeActivity.this);
			return;
		}
		
		if ( service.getCallMeFormat().isEmpty() ) {
			Toast.makeText(this, getString(R.string.toast_callme_not_supported), Toast.LENGTH_LONG).show();
			return;
		}
		
		CallUtil.callme(this, service, CALLME);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data) {
		String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
		
		if (  resultCode == RESULT_OK ) {
			Database.saveSendingCallMe(this, number);
			Toast.makeText(this, getString(R.string.toast_callme_sending), Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, getString(R.string.toast_callme_sending_error) + number, Toast.LENGTH_LONG).show();
		}		
	}
}
