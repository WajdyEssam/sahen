package com.malaz.util;

import android.content.Context;

public class EnvironmentUtil {
	public static String getApplicationInformation(Context context) {
		return String.format("%s\n$s\n%s\n%s\n%s\n%s\n", 
				getCountry(context), getBrandInfo(), getModelInfo(),
				getDeviceInfo(), getVersionInfo(context), 
				getLocale(context));
	}
	
	private static String getCountry(Context context) {
		return "";
	}
	
	private static String getBrandInfo() {
		return "";
	}
	
	private static String getDeviceInfo() {
		return "";
	}
	
	private static String getVersionInfo(Context context) {
		return "";
	}
	
	private static String getModelInfo() {
		return "";
	}
	
	private static String getLocale(Context context) {
		return "";
	}
}
