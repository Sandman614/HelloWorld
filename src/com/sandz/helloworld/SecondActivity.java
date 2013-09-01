package com.sandz.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SecondActivity extends Activity {

	MaintenanceAdapter adapter = null;
	MaintenanceHelper helper = null;
	Cursor dataset_cursor = null;
	
	EditText editMaint;
	EditText editInterval;
	
	AlertDialog alertDialog = null;
	
	String maintID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_second);
//		ListView element, obtained by id
			ListView list = (ListView)findViewById(R.id.maintListView);
//		EditText Element
//			editMaint = (EditText)findViewById(R.id.editMaintText);
//			editInterval = (EditText)findViewById(R.id.editIintervalText);

			helper = new MaintenanceHelper(this);

			/*		
	 		dbMaintenanceHelper.createDatabase();
			dbMaintenanceHelper.openDataBase();
			 */

			dataset_cursor = helper.getAll();

			startManagingCursor(dataset_cursor);

			adapter=new MaintenanceAdapter(dataset_cursor);
			list.setAdapter(adapter);
			
			list.setOnItemClickListener(onListClick);
			
//			Button btnSimple = (Button) findViewById(R.id.add_button);
//			btnSimple.setOnClickListener(onAdd);
			
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
//		helper.insert(editMaint.getText().toString());
//		helper.debugReadValues();
//		dataset_cursor.requery();
//		ourCursor = dbMaintenanceHelper.getAll();
//		editMaint.setText("");
//		editInterval.setText("");
		
		Log.i("Tim", "Entered onAdd()");
		edit_box();
	}

	/*
	 *  Removes a Notification to the database
	 */
	public void removeNotification(View view){

	}
	
	public void onListClick(View view){
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.close();
	}


	private View.OnClickListener onAdd=new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i("Tim", "Entered onAdd()");
			edit_box();
		}
	};
	
	private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			maintID = String.valueOf(id);
			edit_box();
		}
	};
	
	private void edit_box(){
		Log.i("Tim", "Entered on editBow()");
		final View addView = getLayoutInflater().inflate(R.layout.add, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("");
		builder.setView(addView);

		EditText editNote = (EditText)addView.findViewById(R.id.title);

		if(maintID != null){
			Log.i("Tim", "editBox() maintID !=null");
			Cursor c = helper.getByID(maintID);
			c.moveToFirst();
			editNote.setText(helper.getEntry(c,1));
		}
		//  Add button
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				EditText editNote = (EditText)addView.findViewById(R.id.title);
				//save it
				if(maintID==null)
					helper.insert(editNote.getText().toString());
				else{
					helper.update(maintID,editNote.getText().toString());
					//set back to null so next note is new
					maintID = null;
				}
				dataset_cursor.requery();
			}
		});
		//  cancel button
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
			}});
		//  delete button
		builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				EditText editNote = (EditText)addView.findViewById(R.id.title);
				//save it
				if(maintID==null)
					return;
				else{
					helper.delete(maintID);
					//set back to null so next note is new
					maintID = null;
				}
				dataset_cursor.requery();
			}
		});
		//  show dialog
		builder.show();
	}

	class MaintenanceAdapter extends CursorAdapter {

		public MaintenanceAdapter(Cursor c) {
			super(SecondActivity.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor c) {
			MaintenanceHolder holder=(MaintenanceHolder)row.getTag();
			holder.populateFrom(c, helper);
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
			rowMaint.setText(r.getEntry(c, 1));
//			Log.i("Populate","Column Name 1: "+r.getColumnName(c, 1));
//			rowInterval.setText(r.getColumnName(c, 2));
//			Log.i("Populate","Column Name 2: "+r.getColumnName(c, 2));
			
		}
	}

}
