package com.sandz.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Gallagher
 *
 */
public class MaintenanceHelper extends SQLiteOpenHelper {

	private static final int SCHEMA_VERSION = 1;
	private static final String DATABASE_NAME = "maintenance.db";

	private String TABLE_NAME;
	private String COLUMN_ID;
	private String dbColumn1_name;
	private String dbColumn2_name;

	public SQLiteDatabase dbSqlite;


	public MaintenanceHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		TABLE_NAME = context.getResources().getString(R.string.table_name);
		COLUMN_ID = context.getResources().getString(R.string.dbColumn0_name);
		dbColumn1_name = context.getResources().getString(R.string.dbColumn1_name);
		dbColumn2_name = context.getResources().getString(R.string.dbColumn2_name);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + dbColumn1_name + " TEXT, " + dbColumn2_name + " INT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}


	public void insert(String maint, String interval){
			ContentValues cv = new ContentValues();
			cv.put(dbColumn1_name,maint);
			cv.put(dbColumn2_name,interval);
			//		Insert cv key values into the database
			getWritableDatabase().insert(TABLE_NAME, dbColumn1_name, cv);
	}

	public void update(String id, String maint, String interval){
			ContentValues cv = new ContentValues();
			String[] args = {id};
			cv.put(dbColumn1_name, maint);
			cv.put(dbColumn2_name,interval);
			getWritableDatabase().update(TABLE_NAME, cv, COLUMN_ID+"=?", args);
	}

	public void delete(String id){
		ContentValues cv = new ContentValues();
		getWritableDatabase().delete(dbColumn1_name, COLUMN_ID+"=?", new String[] {id});
	}

	public Cursor getAll(){
		return(getReadableDatabase().rawQuery("SELECT "+COLUMN_ID+", "+dbColumn1_name+", "+dbColumn2_name+" FROM "+TABLE_NAME+"", null));
	}

	public Cursor getByID(String id){
		String[] args={id};
		return(getReadableDatabase().rawQuery("SELECT "+COLUMN_ID+", "+dbColumn1_name+", "+dbColumn2_name+" FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"=?", args));
	}

	@Override
	public synchronized void close() {
		if(dbSqlite != null)
			dbSqlite.close();
		super.close();
	}

	/*
	 * Formerly getNote()
	 * Gets column names
	 */
	public String getEntry(Cursor c, int col){
		return(c.getString(col));
	}
}
