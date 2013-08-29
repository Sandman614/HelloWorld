package com.sandz.helloworld;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SecondActivity extends Activity {
	
	private MaintenanceHelper dbMaintenanceHelper = null;
	private Cursor ourCursor = null;
	private MaintenanceAdapter adapter = null;
	
//	ArrayList<String> maintList = new ArrayList<String>();
	EditText editMaint;
	EditText editInterval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_second);
//		ListView element, obtained by id
			ListView myListView = (ListView)findViewById(R.id.maintListView);
//		EditText Element
			editMaint = (EditText)findViewById(R.id.editMaintText);
			editInterval = (EditText)findViewById(R.id.editIintervalText);

			dbMaintenanceHelper = new MaintenanceHelper(this);

			/*		
	 		dbMaintenanceHelper.createDatabase();
			dbMaintenanceHelper.openDataBase();
			 */

			ourCursor = dbMaintenanceHelper.getAll();

			startManagingCursor(ourCursor);
			
//		turn on array adapter
			adapter=new MaintenanceAdapter(ourCursor);
			myListView.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

	/*
	 *  Adds a Notification to the database
	 */
	public void addNotification(View view){
//		dbMaintenanceHelper.debugReadValues();
//		Log.i("Tim", "Line before insert is called.");
		dbMaintenanceHelper.insert(editMaint.getText().toString(), editInterval.getText().toString());
		dbMaintenanceHelper.debugReadValues();
		ourCursor.requery();
//		ourCursor = dbMaintenanceHelper.getAll();
		editMaint.setText("");
//		editInterval.setText("");
	}

	/*
	 *  Removes a Notification to the database
	 */
	public void removeNotification(View view){

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbMaintenanceHelper.close();
	}



	class MaintenanceAdapter extends CursorAdapter {

		public MaintenanceAdapter(Cursor c) {
			super(SecondActivity.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor c) {
			MaintenanceHolder holder=(MaintenanceHolder)row.getTag();
			holder.populateFrom(c, dbMaintenanceHelper);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflator = getLayoutInflater();
			View row=inflator.inflate(R.layout.custom_list_item, parent, false);
			MaintenanceHolder holder = new MaintenanceHolder(row);
			row.setTag(holder);
			return (row);
		}
		
	}
	
/*	class MaintenanceAdapter extends ArrayAdapter<String> {
		
		public MaintenanceAdapter() {
			super(SecondActivity.this, android.R.layout.simple_list_item_1, maintList);
		}

		 (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MaintenanceHolder holder;
			
			if(convertView==null){
				LayoutInflater inflator = getLayoutInflater();
				convertView=inflator.inflate(R.layout.custom_list_item, null);
				
				holder = new MaintenanceHolder(convertView);
				
				convertView.setTag(holder);
			}
			else{
				holder=(MaintenanceHolder)convertView.getTag();
			}
			holder.populateFrom(maintList.get(position));
			
			return(convertView);
		}

	}*/

	static class MaintenanceHolder{
		public TextView rowMaint = null;
//		public TextView rowInterval = null;

		MaintenanceHolder(View row){
			rowMaint = (TextView)row.findViewById(R.id.rowMaintView);
//			rowInterval = (TextView)row.findViewById(R.id.rowIntervalView);
		}
		void populateFrom(Cursor c, MaintenanceHelper r){
			rowMaint.setText(r.getColumnName(c, 1));
//			Log.i("Populate","Column Name 1: "+r.getColumnName(c, 1));
//			rowInterval.setText(r.getColumnName(c, 2));
//			Log.i("Populate","Column Name 2: "+r.getColumnName(c, 2));
			
		}
	}

}
