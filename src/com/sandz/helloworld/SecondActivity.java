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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Gallagher
 *
 */
public class SecondActivity extends Activity {

	private MaintenanceAdapter adapter = null;
	private MaintenanceHelper helper = null;
	private Cursor dataset_cursor = null;

	private String maintID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_second);
			//		ListView element, obtained by id
			ListView list = (ListView)findViewById(R.id.maintListView);

			helper = new MaintenanceHelper(this);

			dataset_cursor = helper.getAll();

			startManagingCursor(dataset_cursor);

			// this be broke.
			adapter=new MaintenanceAdapter(dataset_cursor);
			list.setAdapter(adapter);

			list.setOnItemClickListener(onListClick);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.wtf("Tim", "Something is broken");
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.action_settings) {
			edit_box();
			return(true);
		}
		return (super.onOptionsItemSelected(item));
	}

	/*
	 *  Adds a Notification to the database
	 */
	public void addNotification(View view){		
		//		Log.i("Tim", "Entered onAdd()");
		edit_box();
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
		helper.close();
	}

	private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			maintID = String.valueOf(id);
			edit_box();
		}
	};

	private void errorBox(){
		AlertDialog.Builder errorPopup = new AlertDialog.Builder(this);
		errorPopup.setTitle("Blank entry");
		errorPopup.setMessage("Entries cannot be blank.");
		errorPopup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				//				Return
			}
		});
		errorPopup.show();
	}

	private void edit_box(){
		final View addView = getLayoutInflater().inflate(R.layout.add, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Your Maintenance");
		builder.setView(addView);

		EditText editNote1 = (EditText)addView.findViewById(R.id.title);
		EditText editNote2 = (EditText)addView.findViewById(R.id.title2);

		//		Edit Entry
		if(maintID != null){
			Cursor c = helper.getByID(maintID);
			c.moveToFirst();
			editNote1.setText(helper.getEntry(c,1));
			editNote2.setText(helper.getEntry(c,2));
		}

		//		Add Entry Button
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				EditText editNote1 = (EditText)addView.findViewById(R.id.title);
				EditText editNote2 = (EditText)addView.findViewById(R.id.title2);
				//save it
				String maint = editNote1.getText().toString();
				String interval = editNote2.getText().toString();

				if(maint.length()>0 && interval.length()>0){
				if(maintID==null){
					helper.insert(maint, interval);
				}
				else{
					helper.update(maintID,maint, interval);
					//set back to null so next note is new
					maintID = null;
				}
				dataset_cursor.requery();
				}
				else
					errorBox();
			}
		});

		//		cancel button
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
			}});

		//		delete entry button
		builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				if(maintID==null)
					return;
				else{
					helper.delete(maintID);
					//set back to null so next entry is new
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


	static class MaintenanceHolder{
		private TextView rowMaint = null;
		private TextView rowInterval = null;

		MaintenanceHolder(View row){
			rowMaint = (TextView)row.findViewById(R.id.rowMaintView);
			rowInterval = (TextView)row.findViewById(R.id.rowIntervalView);
		}
		void populateFrom(Cursor c, MaintenanceHelper r){
//			Log.i("Populate","Column Name 0: "+r.getEntry(c, 0));
			rowMaint.setText(r.getEntry(c, 1));
//			Log.i("Populate","Column Name 1: "+r.getEntry(c, 1));
			rowInterval.setText(r.getEntry(c, 2));
//			Log.i("Populate","Column Name 2: "+r.getEntry(c, 2));
			
		}
	}

}
