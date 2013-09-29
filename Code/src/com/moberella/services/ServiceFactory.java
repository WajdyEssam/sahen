package com.moberella.services;

import android.app.Activity;

import com.moberella.util.Constants;
import com.moberella.util.Preferences;

public class ServiceFactory {
	private final String companyName ;
	private final String simNo;
	private final String userIdentityNumber;
	private final int companyId;
	
	public ServiceFactory(Activity activity) {
		this.companyName = Preferences.getValue(activity, Constants.COMPANY_NAME, Constants.DEFAULT_COMPANY_NAME);
		this.companyId = Integer.parseInt(companyName);		
		this.simNo = Preferences.getValue(activity, Constants.SIM_NUMBER, Constants.DEFAULT_SIM_NUMBER);	
		this.userIdentityNumber = Preferences.getValue(activity, Constants.USER_IDENTITY,  Constants.DEFAULT_USER_IDENTITY);
	}
	
	public SIMService getCurrentBalanceService() {
		SIMService service = null;
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 1:
				service =  new MTNService("", "", "");
				break;
				
			case 2:
				service = new SudaniService("", "", "");
				break;
				
			case 3:
				service = new ZainService("", "", "", "");
				break;
				
			}
		}
		
		return service;
	}
	
	
	public SIMService getChargeService(String number) {
		SIMService service = null;
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 1:
				service =  new MTNService(number, "", "");
				break;
				
			case 2:
				service = new SudaniService(number, "", "");
				break;
				
			case 3:
				service = new ZainService(number, "", "", "");
				break;
			}
		}
		
		return service;
	}
	
		
	public SIMService getCallMeService(String number) {
		SIMService service = null;
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 1:
				service =  new MTNService("", number, "");
				break;
				
			case 2:
				service = new SudaniService("", number, "");
				break;
				
			case 3:
				service = new ZainService("", number, "", "");
				break;
			}
		}
		
		return service;
	}
		
	public SIMService getSendBalanceService(String balance, String number) {
		SIMService service = null;
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 1:
				service =  new MTNService("", number, balance);
				break;
				
			case 2:
				service = new SudaniService("", number, balance);
				break;
				
			case 3:
				service = new ZainService("", number, balance, simNo);
				break;
			}
		}
		
		return service;
	}
	
}
