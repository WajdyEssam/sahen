package com.malaz.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.malaz.model.Operation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OperationDB {

	private final static String ID = "ID";	
	private final static String ENGLISH_DESCRIPTION = "EnglishDescription";
	private final static String ARABIC_DESCRIPTION = "ArabicDescription";
	
	private final static String[] COLUMNS_NAMES = {
		ID, ENGLISH_DESCRIPTION, ARABIC_DESCRIPTION
	};
	
	public final static String DATABASE_TABLE = "Operations";
	
	public final static String DATABASE_CREATE = "create table " + DATABASE_TABLE +
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
	
	public long getNumberOfRecords() {
		return DatabaseUtils.queryNumEntries(this.open(), DATABASE_TABLE);
	}
	
	public long insertOperation(Operation operation) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, operation.getId());
		initialValues.put(ENGLISH_DESCRIPTION, operation.getEnglishDescription());
		initialValues.put(ARABIC_DESCRIPTION, operation.getArabicDescription());
		
		return this.open().insert(DATABASE_TABLE, null, initialValues);				
	}
	
	public boolean deleteOperation(String rowId) {
		String where = String.format("%s = %s", ID, rowId);
		return this.open().delete(DATABASE_TABLE, where, null) > 0;
	}
	
	public List<Operation> getAllOperations() {
		List<Operation> operatoins = new ArrayList<Operation>();
		Cursor cursor =  this.open().query(DATABASE_TABLE, COLUMNS_NAMES,  null, null, null, null, null);
		
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
		Cursor cursor = this.open().query(true, DATABASE_TABLE, COLUMNS_NAMES, where, null, null, null, null, null);
		Operation operation = null;
		
		if ( cursor.moveToFirst() ) {
			operation = getObject(cursor);
		}
		
		cursor.close();		
		return operation;
	}
	
	private Operation getObject(Cursor cursor) {
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
		return this.open().update(DATABASE_TABLE, args,  where, null) > 0;
	}
	
	public void addInitialData() {
		insertOperation(Operation.getInstance("1", "Charging", "شحن"));
		insertOperation(Operation.getInstance("2", "Transfere", "تحويل"));
		insertOperation(Operation.getInstance("3", "CallMe", "كول مي"));
	}
	
	public void clearOperations() {
		List<Operation> operations = getAllOperations();
		for(Operation opration: operations) {
			deleteOperation(opration.getId());
		}
	}
}
