package com.sandz.helloworld;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MaintenanceHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_PATH = Integer.toString(R.string.database_path);
	private static final String DATABASE_NAME = Integer.toString(R.string.database_name);
	private static final int SCHEMA_VERSION = R.string.schema_version;
	
	private final Context myContext;
	
	public SQLiteDatabase dbSqlite;
	

	public MaintenanceHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		this.myContext = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void createDatabase(){
		createDB();
	}
	
	private void createDB(){
		boolean dbExist = DBExists();
		
		if(!dbExist){
//			Creates an empty database 
			this.getReadableDatabase();
//			Copy the included database
			copyDBFromResource();
			
		}
	}
	
	private boolean DBExists(){
		SQLiteDatabase db = null;
		
		try {
			String databasePath = DATABASE_PATH + DATABASE_NAME;
			db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			db.setLocale(Locale.getDefault());
			db.setLockingEnabled(true);
			db.setVersion(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(db != null)
			db.close();
		
		return db != null ? true : false;
	}
	
	private void copyDBFromResource(){
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String dbFilePath = DATABASE_PATH + DATABASE_NAME;
		
		try {
			inputStream = myContext.getAssets().open(DATABASE_NAME);
			outputStream = new FileOutputStream(dbFilePath);
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length=inputStream.read(buffer))>0){
				outputStream.write(buffer, 0, length);
			}
			
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
