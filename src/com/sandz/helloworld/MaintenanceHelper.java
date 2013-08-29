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
	
//	private static String DATABASE_PATH;
	private String TABLE_NAME;
	private String COLUMN_ID;
	private String COLUMN1_NAME;
	private String COLUMN2_NAME;

	public SQLiteDatabase dbSqlite;


	public MaintenanceHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
//		DATABASE_PATH = context.getResources().getString(R.string.database_path);
		TABLE_NAME = context.getResources().getString(R.string.table_name);
		COLUMN_ID = context.getResources().getString(R.string.column_id);
		COLUMN1_NAME = context.getResources().getString(R.string.column1_name);
		COLUMN2_NAME = context.getResources().getString(R.string.column2_name);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE '"+TABLE_NAME+"'('"+COLUMN_ID+"' INTEGER PRIMARY KEY AUTOINCREMENT, '"+COLUMN1_NAME+"' TEXT, '"+COLUMN2_NAME+"' INT);");
	}

	public void debugReadValues(){
//		Log.e("SQL","CREATE TABLE '"+TABLE_NAME+"'('"+COLUMN_ID+"' INTEGER PRIMARY KEY AUTOINCREMENT, '"+COLUMN1_NAME+"' TEXT, '"+COLUMN2_NAME+"' INT);");
		Log.i("SQL","Constant TABLE_NAME = "+TABLE_NAME);
//		Log.i("SQL","XML R.string.table_name = "+R.string.table_name);
		Log.i("SQL","Constant COLUMN1_NAME = "+COLUMN1_NAME);
//		Log.i("SQL","XML R.string.column1_name = "+R.string.column1_name);
//		Log.i("SQL","Constant COLUMN2_NAME = "+COLUMN2_NAME);
//		Log.i("SQL","XML R.string.column2_name = "+R.string.column2_name);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void insert(String maint, String interval){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN1_NAME,maint);
//		cv.put(COLUMN2_NAME,interval);
		//		Insert cv key values into the database
		getWritableDatabase().insert(TABLE_NAME, COLUMN1_NAME, cv);
	}

	public Cursor getAll(){
		return(getReadableDatabase().rawQuery("SELECT '"+COLUMN_ID+"', '"+COLUMN1_NAME+"' FROM '"+TABLE_NAME+"'", null));
	}
	/*
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
	 */
	
	/*
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
	
	 */
	
	/*
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

	public void openDataBase() throws SQLException{
		String myPath = DATABASE_PATH + DATABASE_NAME;
		dbSqlite = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	 */

	@Override
	public synchronized void close() {
		if(dbSqlite != null)
			dbSqlite.close();
		super.close();
	}
	/*
	public Cursor getCursor() {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TABLE_NAME);
		String[] asColumnsToReturn = new String[]{COLUMN_ID, COLUMN1_NAME, COLUMN2_NAME};
		Cursor mCursor = queryBuilder.query(dbSqlite, asColumnsToReturn, null, null, null, null, COLUMN1_NAME + " ASC");
		return mCursor;
	}
	 */
	
	/*
	 * Formerly getNote()
	 * Gets column names
	 */
	public String getColumnName(Cursor c, int col){
		return(c.getString(col));
	}
}
