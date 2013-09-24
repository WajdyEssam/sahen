package com.moberella.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.moberella.services.SIMService;

public class CallUtil {

	public static boolean charge(Activity ac, SIMService service, int requestCode) {
		boolean state = call(ac, service.getChargeFormat(), requestCode);
		return state;
	}
	
	public static boolean convert(Activity ac, SIMService service, int requestCode) {
		boolean state = call(ac, service.getSendBalance(), requestCode);
		return state;
	}
	
	public static boolean callme(Activity ac,  SIMService service, int requestCode) {
		boolean state = call(ac, service.getCallMeFormat(), requestCode);
		return state;
	}
	
	public static boolean current(Activity ac, SIMService service, int requestCode) {
		boolean state = call(ac, service.getCurrentBalance(),requestCode);
		return state;	
	}
	
	private static boolean call(Activity ac, String number, int requestCode) {
		boolean state = true;
		
		try {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + number));
			ac.startActivityForResult(intent, requestCode);
		}
		catch(Exception e) {
			state = false;
		}
		
		return state;
	}
}
