package com.moberella.util;

import android.content.Context;

import com.moberella.util.LangUtil.Languages;

public class AppUtil {
	public static CharSequence[] englishItems = {  "Unknown", "MTN", "Sudani", "Zain", };
	public static CharSequence[] arabicItems = { "غير معرف", "ام تي ان", "سوداني", "زين",  };
	
	public static String getSIMName(String id, Context context) {
		int index = 0;
		
		try {
			index = Integer.parseInt(id);
		}
		catch(Exception e) {			
		}
		
		if ( index > englishItems.length - 1) 
			index = 0;
		
		String name = "";
		
		if ( LangUtil.getCurrentLanguage(context) == Languages.English ) { 
			name = englishItems[index].toString();
		}
		else {
			name = arabicItems[index].toString();
		}
		
		return name;
	}
}
