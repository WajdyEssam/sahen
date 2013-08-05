package com.malaz.app;

import java.util.Date;

import com.malaz.database.Database;
import com.malaz.database.HistoryDB;
import com.malaz.database.OperationDB;
import com.malaz.model.History;
import com.malaz.model.Operation;
import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;
import com.malaz.util.Constants;
import com.malaz.util.LangUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
		String number = numberEditText.getText().toString();
		
		SIMService service = new ServiceFactory(this).getCallMeService(number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(CallmeActivity.this);
			return;
		}
		
		boolean state = CallUtil.callme(this, service);

		if ( state ) {
			Database.saveSendingCallMe(this, number);
			Toast.makeText(this, "Callme is sending successfully!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Error in sending call me message", Toast.LENGTH_LONG).show();
		}
	}
}
