package com.malaz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
final class DBHelper extends SQLiteOpenHelper {
	
	private final static String TAG = "DBAdapter";
	private final static String DATABASE_NAME = "SahenDB";	
	private final static int DATABASE_VERSION = 1;
	private static DBHelper instance = null;
	
	public static DBHelper getInstance(Context context) {
		if ( instance == null ) {
			instance = new DBHelper(context);
		}
		
		return instance;
	}
	
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(HistoryDB.DATABASE_CREATE);
		db.execSQL(OperationDB.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");			
		db.execSQL("DROP TABLE IF EXISTS " +  HistoryDB.DATABASE_TABLE);
		onCreate(db);			
	}		
}
