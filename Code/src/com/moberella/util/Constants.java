package com.moberella.util;

import android.os.Environment;

public class Constants {
	public final static String CHARGING_OPERATION = "1";
	public final static String SENDING_BALANCE_OPERATION = "2";
	public final static String SENDING_CALLME_OPERATION  = "3";
	
	public final static String CHARGING_OPERATION_ARABIC_MSG = "تم شحن الرصيد  ";
	public final static String CHARGING_OPERATION_ENGLISH_MSG = "charging balance";
	
	public final static String SENDING_BALANCE_OPERATION_ARABIC_MSG = "تم تحويل الرصيد  ";
	public final static String SENDING_BALANCE_OPERATION_ENGLISH_MSG = "sending balance";
	
	public final static String SENDING_CALLME_OPERATION_ARABIC_MSG = "تم ارسال رسالة كلمني";
	public final static String SENDING_CALLME_OPERATION_ENGLISH_MSG = "sending callme message";
	
	// Preferences Constants
	public final static String FILE_NAME = "sahen_sudani";
	
	public final static String COMPANY_NAME = "companyName";
	public final static String DEFAULT_COMPANY_NAME = "";	// stored as id number
	
	public final static String SIM_NUMBER = "simNo";
	public final static String DEFAULT_SIM_NUMBER = "";
	
	public final static String APPLICATION_LANGUAGE = "applicationlanguage";
	public final static String DEFAULT_APPLICATION_LANGUAGE = "1"; // stored as id number
		
	// Application file paths
	public final static String APPLICATION_FOLDER = Environment.getExternalStorageDirectory().toString() + "/SahenZakiOCR/";
	public final static String DATASET_FOLDER = APPLICATION_FOLDER + "tessdata/";		
	public final static String DATASET_DESTINATION_FILE = DATASET_FOLDER + "eng.traineddata";
	public final static String DATASET_SOURCE_FILE = "tessdata/eng.traineddata";
}
