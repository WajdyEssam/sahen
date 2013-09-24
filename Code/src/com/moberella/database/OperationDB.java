package com.moberella.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.moberella.model.Operation;

public class OperationDB {

	public final static String ID = "ID";	
	private final static String ENGLISH_DESCRIPTION = "EnglishDescription";
	private final static String ARABIC_DESCRIPTION = "ArabicDescription";
	
	private final static String[] COLUMNS_NAMES = {
		ID, ENGLISH_DESCRIPTION, ARABIC_DESCRIPTION
	};
	
	public final static String Operation_TABLE = "Operations";
	
	public final static String CREATE_OPERATION_TABLE = "create table " + Operation_TABLE +
			" (" + ID + " TEXT primary key, "
			+ ENGLISH_DESCRIPTION + " text not null, " 
			+ ARABIC_DESCRIPTION + " text not null );";
	
	private DBHelper dbHelper;
		
	public static OperationDB getInstance(Context context) {
		return new OperationDB(context);
	}
	
	private OperationDB(Context context) {
		this.dbHelper = DBHelper.getInstance(context);
	}
	
	public SQLiteDatabase open() {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		return db;
	}
	
	public long insertOperation(Operation operation) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, operation.getId());
		initialValues.put(ENGLISH_DESCRIPTION, operation.getEnglishDescription());
		initialValues.put(ARABIC_DESCRIPTION, operation.getArabicDescription());
		
		return this.open().insert(Operation_TABLE, null, initialValues);				
	}
	
	public List<Operation> getAllOperations() {
		List<Operation> operatoins = new ArrayList<Operation>();
		Cursor cursor =  this.open().query(Operation_TABLE, COLUMNS_NAMES,  null, null, null, null, null);
		
		if ( cursor.moveToFirst()) {
			do {
				operatoins.add(getObject(cursor));
            } while (cursor.moveToNext());
		}
		
		cursor.close();
		return operatoins;
	}
	
	public Operation getOperation(String rowId) throws SQLException {
		String where = String.format("%s = %s", ID, rowId);
		Cursor cursor = this.open().query(true, Operation_TABLE, COLUMNS_NAMES, where, null, null, null, null, null);
		Operation operation = null;
		
		if ( cursor.moveToFirst() ) {
			operation = getObject(cursor);
		}
		
		cursor.close();		
		return operation;
	}
	
	private static Operation getObject(Cursor cursor) {
		String id = cursor.getString(cursor.getColumnIndex(ID));
		String englishName = cursor.getString(cursor.getColumnIndex(ENGLISH_DESCRIPTION));
		String arabicName = cursor.getString(cursor.getColumnIndex(ARABIC_DESCRIPTION));
		
		return Operation.getInstance(id, englishName, arabicName);
	}
	
	public boolean updateOperation(String rowId, Operation operation) {
		ContentValues args = new ContentValues();
		args.put(ID, operation.getId());
		args.put(ENGLISH_DESCRIPTION, operation.getEnglishDescription());
		args.put(ARABIC_DESCRIPTION, operation.getArabicDescription());
		
		String where = String.format("%s = %s", ID, rowId);
		return this.open().update(Operation_TABLE, args,  where, null) > 0;
	}
	
	// this is called when creating database from onCreate method
	public static void addInitialData(SQLiteDatabase db) {
		insertOperation(db, Operation.getInstance("1", "Charging", "شحن"));
		insertOperation(db, Operation.getInstance("2", "Transfere", "تحويل"));
		insertOperation(db, Operation.getInstance("3", "CallMe", "كول مي"));
	}
	
	private static long insertOperation(SQLiteDatabase db, Operation operation) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, operation.getId());
		initialValues.put(ENGLISH_DESCRIPTION, operation.getEnglishDescription());
		initialValues.put(ARABIC_DESCRIPTION, operation.getArabicDescription());
		
		return db.insert(Operation_TABLE, null, initialValues);				
	}
	
	// this called from HistoryDB when getting operation information 
	public static Operation getOperation(SQLiteDatabase db, String rowId) throws SQLException {
		String where = String.format("%s = %s", ID, rowId);
		Cursor cursor = db.query(true, Operation_TABLE, COLUMNS_NAMES, where, null, null, null, null, null);
		Operation operation = null;
		
		if ( cursor.moveToFirst() ) {
			operation = getObject(cursor);
		}
		
		cursor.close();		
		return operation;
	}
}
