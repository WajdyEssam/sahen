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

public class ConvertActivity extends BaseActivity {

	private EditText numberEditText;
	private EditText balanceEditText;
	
	private final static int TRANSFERE_BALANCE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_convert);
		this.initializingActionBar();
		
		numberEditText = (EditText) findViewById(R.id.otherNumberEditText);
		balanceEditText = (EditText) findViewById(R.id.balanceEditText);
	}
	
	public void convertBalanceButtonClicked(View view) {
		String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
		String balance = balanceEditText.getText().toString().trim().replaceAll(" ", "");
		
		if ( number.isEmpty() || balance.isEmpty() ) {
			Toast.makeText(this, "Please Write All Nesseccary Information Before Transfering", Toast.LENGTH_LONG).show();
			return;
		}
		
		SIMService service = new ServiceFactory(this).getSendBalanceService(balance, number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(ConvertActivity.this);
			return;
		}
		
		CallUtil.convert(this, service, TRANSFERE_BALANCE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data) {
		String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
		String balance = balanceEditText.getText().toString().trim().replaceAll(" ", "");
		
		if (  resultCode == RESULT_OK ) {
			Database.saveSendingBalance(this, number, balance);			
			Toast.makeText(this, "Transfering " + balance + " To " + number + " is Done!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Transfering Error in Balance "  + balance + " To " + number, Toast.LENGTH_LONG).show();
		}			
	}
}
