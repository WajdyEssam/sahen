package com.moberella.util;

import com.moberella.app.BuildConfig;

import android.util.Log;

/**
 * @author WajdyEssam
 *
 */
public final class Logger {
	private static final String LOGGER_TAG = "SAHEN_ZAKI";
	
	public static void show(String format, Object...args){
		if ( !BuildConfig.DEBUG ) {
			return;
		}
		
		Log.d(LOGGER_TAG, getLogging(format, args));
	}
	
	public static void showInDebugOnly(String format, Object... args) {
		Log.i(LOGGER_TAG, getLogging(format, args));
	}
	
	private static String getLogging(String format, Object... args) {
		if ( args.length == 0 ) {
			return format;
		}
		
		return String.format(format, args);
	}
}
