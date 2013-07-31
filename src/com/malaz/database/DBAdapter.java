package com.malaz.database;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public final static String KEY_ROWID = "_id";
	public final static String KEY_DESCRIPTION_ARABIC  = "description_arabic";
	public final static String KEY_DESCRIPTION_ENGLISH = "description_english";
	public final static String KEY_TIME = "curren_time";
	public final static String KEY_TYPE = "type";
	
	private final static String TAG = "DBAdapter";
	private final static String DATABASE_NAME = "SahenDB";
	private final static String DATABASE_TABLE = "histories";
	private final static int DATABASE_VERSION = 1;
	
	private final static String DATABASE_CREATE = "create table " + DATABASE_TABLE +
			" (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_DESCRIPTION_ARABIC + " text not null, " 
			+ KEY_DESCRIPTION_ENGLISH + " text not null, "
			+ KEY_TIME + " date not null, "
			+ KEY_TYPE + " integer not null );";
	
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context context) {
		this.context = context;
		this.DBHelper = new DatabaseHelper(this.context);
	}
	
	public DBAdapter open() throws SQLException {
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.DBHelper.close();
	}
	
	public void addTestData() {
		insertRecord("Wajdy", "Essam", new Date(), 1);
		insertRecord("Malaz", "Mustafa", new Date(), 2);
		insertRecord("Ahmed", "Ali", new Date(), 3);
	}
	
	public void clearDB() {
		Cursor cursor = getAllRecords();
		if ( cursor.moveToFirst() ) {
			do {
				deleteRecord(Integer.valueOf(cursor.getString(0)));
			}while(cursor.moveToNext());
		}
		
		cursor.close();
	}
	
	public long getNumberOfRecords() {
		return DatabaseUtils.queryNumEntries(this.db, DATABASE_TABLE);
	}
	
	public long insertRecord(String arabicDecs, String englishDesc, Date time, int type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DESCRIPTION_ARABIC, arabicDecs);
		initialValues.put(KEY_DESCRIPTION_ENGLISH, englishDesc);
		initialValues.put(KEY_TIME, time.toString());
		initialValues.put(KEY_TYPE, type);
		
		return this.db.insert(DATABASE_TABLE, null, initialValues);				
	}
	
	public boolean deleteRecord(long rowId) {
		return this.db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor getAllRecords() {
		return this.db.query(DATABASE_TABLE, 
				new String[] {KEY_ROWID,  KEY_DESCRIPTION_ARABIC,  KEY_DESCRIPTION_ENGLISH,  KEY_TIME,  KEY_TYPE}, 
				null, null, null, null, null);
	}
	
	public Cursor getRecord(long rowId) throws SQLException {
		Cursor mCursor = this.db.query(true, DATABASE_TABLE,
				new String[] {KEY_ROWID,  KEY_DESCRIPTION_ARABIC,  KEY_DESCRIPTION_ENGLISH,  KEY_TIME,  KEY_TYPE}
				, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		
		if ( mCursor != null ) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public boolean updateRecord(long rowId, String arabicDecs, String englishDesc, Date time, int type) {
		ContentValues args = new ContentValues();
		args.put(KEY_DESCRIPTION_ARABIC, arabicDecs);
		args.put(KEY_DESCRIPTION_ENGLISH, englishDesc);
		args.put(KEY_TIME, time.toString());
		args.put(KEY_TYPE, type);
		
		return this.db.update(DATABASE_TABLE, args,  KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			
			db.execSQL("DROP TABLE IF EXISTS " +  DATABASE_TABLE);
			onCreate(db);			
		}			
	}
}
