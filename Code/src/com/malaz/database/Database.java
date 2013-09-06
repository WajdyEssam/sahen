package com.malaz.database;

import java.util.Date;

import android.app.Activity;

import com.malaz.model.History;
import com.malaz.model.Operation;
import com.malaz.util.Constants;
import com.malaz.util.DateUtil;

public class Database {

	public static void saveSendingBalance(Activity activity, String number, String balance ) {
		HistoryDB db = HistoryDB.getInstance(activity);
		Operation operation = OperationDB.getInstance(activity).getOperation(Constants.SENDING_BALANCE_OPERATION);
		History history = History.getInstance(0, operation, DateUtil.formatDate(new Date()), Integer.valueOf(balance), number);
		db.insertHistory(history);
	}
	
	public static void saveChargingBalance(Activity activity, String number, int amount) {
		HistoryDB db = HistoryDB.getInstance(activity);
		Operation operation = OperationDB.getInstance(activity).getOperation(Constants.CHARGING_OPERATION);
		History history = History.getInstance(0, operation, DateUtil.formatDate(new Date()), amount , number);
		db.insertHistory(history);
	}
	
	public static void saveSendingCallMe(Activity activity, String number) {
		HistoryDB db = HistoryDB.getInstance(activity);
		Operation operation = OperationDB.getInstance(activity).getOperation(Constants.SENDING_CALLME_OPERATION);
		History history = History.getInstance(0, operation, DateUtil.formatDate(new Date()), 0 , number);
		db.insertHistory(history);
	}
}
