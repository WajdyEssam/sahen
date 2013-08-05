package com.malaz.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	public static String getValue(Context context, String key, String defaultValue)  {
		SharedPreferences preference = open(context);
		return preference.getString(key, defaultValue);
	}
	
	public static void setValue(Context context, String key, String value) {
		SharedPreferences.Editor editor = open(context).edit();
		editor.putString(key, value);
		editor.commit();	
	}
	
	private static SharedPreferences open(Context context) {
		return context.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE);
	}
}
