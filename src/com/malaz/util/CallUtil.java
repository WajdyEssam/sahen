package com.malaz.util;

import com.malaz.services.SIMService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CallUtil {

	public static boolean charge(Activity ac, SIMService service) {
		boolean state = call(ac, service.getChargeFormat());
		return state;
	}
	
	public static boolean convert(Activity ac, SIMService service ) {
		boolean state = call(ac, service.getSendBalance());
		return state;
	}
	
	public static boolean callme(Activity ac,  SIMService service) {
		boolean state = call(ac, service.getCallMeFormat());
		return state;
	}
	
	public static boolean current(Activity ac, SIMService service) {
		boolean state = call(ac, service.getBalance());
		return state;	
	}
	
	private static boolean call(Activity ac, String number) {
		boolean state = true;
		
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + number));
			ac.startActivity(intent);
		}
		catch(Exception e) {
			state = false;
		}
		
		return state;
	}
}
