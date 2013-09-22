package com.moberella.util;

import android.content.Context;

import com.moberella.util.LangUtil.Languages;

public class AppUtil {
	public static CharSequence[] englishItems = { "MTN", "Sudani", "Zain", "Unknown" };
	public static CharSequence[] arabicItems = { "ام تي ان", "سوداني", "زين", "غير معر�?" };
	
	public static String getSIMName(String id, Context context) {
		int index = 3;
		
		try {
			index = Integer.parseInt(id);
		}
		catch(Exception e) {			
		}
		
		if ( index > englishItems.length - 1) 
			index = 3;
		
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
