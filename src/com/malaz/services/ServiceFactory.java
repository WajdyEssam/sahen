package com.malaz.services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class ServiceFactory {

	private final String companyName ;
	private final String simNo;
	private final int companyId;
	
	public ServiceFactory(Activity activity) {
		SharedPreferences appPrefs = activity.getSharedPreferences("sahen_sudani", Activity.MODE_PRIVATE);
		companyName = appPrefs.getString("companyName", "3");
		companyId = Integer.parseInt(companyName);		
		simNo = appPrefs.getString("simNo", "");	
	}
	
	public SIMService getCurrentBalanceService() {
		SIMService service = null;
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 0:
				service =  new MTNService("", "", "");
				break;
				
			case 1:
				service = new SudaniService("", "", "");
				break;
				
			case 2:
				service = new ZainService("", "", "", "");
				break;
			}
		}
		
		return service;
	}
	
	
	public SIMService getChargeService(String number) {
		SIMService service = null;
		
		if ( companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 0:
				service =  new MTNService("", number, "");
				break;
				
			case 1:
				service = new SudaniService("", number, "");
				break;
				
			case 2:
				service = new ZainService("", number, "", "");
				break;
			}
		}
		
		return service;
	}
	
		
	public SIMService getCallMeService(String number) {
		SIMService service = null;
		
		if ( companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 0:
				service =  new MTNService("", number, "");
				break;
				
			case 1:
				service = new SudaniService("", number, "");
				break;
				
			case 2:
				service = new ZainService("", number, "", "");
				break;
			}
		}
		
		return service;
	}
		
	public SIMService getSendBalanceService(String balance, String number) {
		SIMService service = null;
		
		if ( companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 0:
				service =  new MTNService("", number, balance);
				break;
				
			case 1:
				service = new SudaniService("", number, balance);
				break;
				
			case 2:
				service = new ZainService("", number, balance, simNo);
				break;
			}
		}
		
		return service;
	}
}
