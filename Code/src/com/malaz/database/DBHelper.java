package com.malaz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
final class DBHelper extends SQLiteOpenHelper {
	
	private final static String TAG = "DBAdapter";
	private final static String DATABASE_NAME = "SahenDB";	
	private final static int DATABASE_VERSION = 3;
	private static DBHelper instance = null;
	
	private Context context;
	
	public static DBHelper getInstance(Context context) {
		if ( instance == null ) {
			instance = new DBHelper(context);
		}
		
		return instance;
	}
	
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(HistoryDB.CREATE_HISTORIES_TABLE);
		db.execSQL(HistoryDB.CREATE_LOGS_TABLE);
		db.execSQL(OperationDB.CREATE_OPERATION_TABLE);				
		OperationDB.addInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");			
		db.execSQL("DROP TABLE IF EXISTS " +  HistoryDB.HISTORIES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " +  HistoryDB.LOGS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " +  OperationDB.Operation_TABLE);
		onCreate(db);			
	}		
}
