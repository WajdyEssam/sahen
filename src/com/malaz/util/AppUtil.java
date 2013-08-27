package com.malaz.util;

public class AppUtil {
	public static CharSequence[] items = { "MTN", "Sudani", "Zain", "Unknown" };
	
	public static String getSIMName(String id) {
		int index = 0;
		
		try {
			index = Integer.parseInt(id);
		}
		catch(Exception e) {			
		}
		
		if ( index > items.length - 1) 
			index = 0;
		
		return items[index].toString();
	}
}
