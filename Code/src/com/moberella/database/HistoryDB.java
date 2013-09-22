package com.moberella.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.moberella.model.History;
import com.moberella.model.Operation;

public class HistoryDB {
	public final static String ID = "_id";
	public final static String OPERATION_ID = "OpeationID";
	public final static String OPEARTION_TIME = "OperationTime";
	public final static String OPEARTION_AMOUNT = "OperationAmount";
	public final static String DETIALS = "Detials";
	
	public final static String HISTORIES_TABLE = "histories";
	public final static String LOGS_TABLE = "logs";	
	
	public final static String CREATE_HISTORIES_TABLE = "create table " + HISTORIES_TABLE +
			" (" 
				+ ID + " integer primary key autoincrement, "
				+ OPERATION_ID + " text not null, " 
				+ OPEARTION_TIME + " text not null, "
				+ OPEARTION_AMOUNT + " integer, "
				+ DETIALS + " text, "
				+ "FOREIGN KEY (" + OPERATION_ID + ") REFERENCES "+ OperationDB.Operation_TABLE + " (" + OperationDB.ID + ") "
			+ ");";
	
	public final static String CREATE_LOGS_TABLE = "create table " + LOGS_TABLE +
			" (" 
				+ ID + " integer primary key autoincrement, "
				+ OPERATION_ID + " text not null, " 
				+ OPEARTION_TIME + " text not null, "
				+ OPEARTION_AMOUNT + " integer, "
				+ DETIALS + " text, "
				+ "FOREIGN KEY (" + OPERATION_ID + ") REFERENCES "+ OperationDB.Operation_TABLE + " (" + OperationDB.ID + ") "
			+ ");";
	
	private final static String[] COLUMNS_NAMES = {
		ID, OPERATION_ID, OPEARTION_TIME, OPEARTION_AMOUNT, DETIALS
	};
	
	private DBHelper dbHelper;
	
	public static HistoryDB getInstance(Context context) {
		return new HistoryDB(context);
	}
	
	private HistoryDB(Context context) {
		this.dbHelper = DBHelper.getInstance(context);		
	}
	
	public SQLiteDatabase open() {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		return db;
	}
	
	public long getNumberOfLogs() {
		return DatabaseUtils.queryNumEntries(this.open(), LOGS_TABLE);
	}
	
	public long insertHistory(History history) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(OPERATION_ID, history.getOperation().getId());
		initialValues.put(OPEARTION_TIME, history.getTime());
		initialValues.put(OPEARTION_AMOUNT, history.getAmount());
		initialValues.put(DETIALS, history.getDetials());
		
		return this.open().insert(HISTORIES_TABLE, null, initialValues);				
	}
	
	public long insertLogs(History history) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(OPERATION_ID, history.getOperation().getId());
		initialValues.put(OPEARTION_TIME, history.getTime());
		initialValues.put(OPEARTION_AMOUNT, history.getAmount());
		initialValues.put(DETIALS, history.getDetials());
		
		return this.open().insert(LOGS_TABLE, null, initialValues);				
	}
		
	public boolean deleteHistory(int rowId) {
		String where = String.format("%s = %s", ID, rowId);
		return this.open().delete(HISTORIES_TABLE, where, null) > 0;
	}
	
	public boolean deleteLog(int rowId) {
		String where = String.format("%s = %s", ID, rowId);
		return this.open().delete(LOGS_TABLE, where, null) > 0;
	}
	
	public List<History> getAllLogs() {
		List<History> histories = new ArrayList<History>();
		
		Cursor cursor =  this.open().query(LOGS_TABLE, COLUMNS_NAMES,  null, null, null, null, null);
		
		if ( cursor.moveToFirst()) {
			do {
				histories.add(getObject(cursor));
            } while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return histories;
	}
	
	private History getObject(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(ID));
		String operationId = cursor.getString(cursor.getColumnIndex(OPERATION_ID));
		String time = cursor.getString(cursor.getColumnIndex(OPEARTION_TIME));
		String amount = cursor.getString(cursor.getColumnIndex(OPEARTION_AMOUNT));
		String detials = cursor.getString(cursor.getColumnIndex(DETIALS));
		
		if ( amount == null ) 
			amount = "0";
				
		Operation operation = OperationDB.getOperation(this.open(), operationId);		
		return History.getInstance(id, operation, time, Integer.parseInt(amount), detials);
	}
	
	// data format is YYYY-MM-dd HH:MM:ss for example '2013-08-24 23:59:59'	
	public List<History> getHistoriesBetween(String fromDate, String toDate) {
		List<History> histories = new ArrayList<History>();
		
		String where = String.format("%s BETWEEN '%s' AND '%s'", OPEARTION_TIME, fromDate, toDate);
		Cursor cursor =  this.open().query(HISTORIES_TABLE, COLUMNS_NAMES,  where, null, null, null, null);
		
		if ( cursor.moveToFirst()) {
			do {
				histories.add(getObject(cursor));
            } while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return histories;
	}
	
	public History getHistory(long rowId) throws SQLException {
		String where = String.format("%s = %s", ID, rowId);
		Cursor cursor = this.open().query(true, HISTORIES_TABLE, COLUMNS_NAMES, where, null, null, null, null, null);
		History history = null;
		
		if ( cursor.moveToFirst() ) {
			history = getObject(cursor);
		}
		
		cursor.close();		
		return history;
	}
	
	public boolean updateHistory(int rowID, History history) {
		ContentValues args = new ContentValues();
		args.put(OPERATION_ID, history.getOperation().getId());
		args.put(OPEARTION_TIME, history.getTime());
		args.put(OPEARTION_AMOUNT, history.getAmount());
		args.put(DETIALS, history.getDetials());
		
		String where = String.format("%s = %s", ID, rowID);
		return this.open().update(HISTORIES_TABLE, args,  where, null) > 0;
	}
	
	public void clearHistories() {
		List<History> histories = getAllLogs();
		for(History history: histories) {
			deleteHistory(history.getId());
		}
	}
	
	public void clearLogs() {
		List<History> histories = getAllLogs();
		for(History history: histories) {
			deleteLog(history.getId());
		}
	}
}
