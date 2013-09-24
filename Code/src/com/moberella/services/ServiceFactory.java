package com.moberella.services;

import android.app.Activity;

import com.moberella.util.Constants;
import com.moberella.util.Preferences;

public class ServiceFactory {
	private final String companyName ;
	private final String simNo;
	private final int companyId;
	
	public ServiceFactory(Activity activity) {
		this.companyName = Preferences.getValue(activity, Constants.COMPANY_NAME, "3");
		this.companyId = Integer.parseInt(companyName);		
		this.simNo = Preferences.getValue(activity, Constants.SIM_NUMBER, "");	
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
		
		if ( !companyName.isEmpty() ) {
			
			switch ( companyId ) {
			case 0:
				service =  new MTNService(number, "", "");
				break;
				
			case 1:
				service = new SudaniService(number, "", "");
				break;
				
			case 2:
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
		
		if ( !companyName.isEmpty() ) {
			
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
