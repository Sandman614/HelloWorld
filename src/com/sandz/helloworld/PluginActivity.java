package com.sandz.helloworld;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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

import com.sandz.torque.remote.ITorqueService;

/**
 * @author Gallagher
 *
 */
public class PluginActivity extends Activity {

	private MaintenanceAdapter adapter = null;
	private MaintenanceHelper helper = null;
	private Cursor dataset_cursor = null;
	private String maintID = null;
	//	Vehicle distance (Odometer) saved with profile
	private final String MILEAGE = "0ff120C";
	private Timer updateTimer;

	private ITorqueService torqueService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.main);
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
			catchException(e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Bind to the torque service
		Log.i("Tim", "onResume() attempting to bindservice");
		Intent intent = new Intent();
		intent.setClassName("com.sandz.torque", "com.sandz.torque.remote.TorqueService");
		Log.i("Tim", "Stop after setclassname.");
		boolean successfulBind = bindService(intent, connection, 0);
		Log.i("Tim", "Stop after bindservice."+ successfulBind);

		if (successfulBind) {
			Log.i("Tim", "Succesful bind to torque service.");
//			updateTimer = new Timer();
//			updateTimer.schedule(new TimerTask() { public void run() {
////			update();
//				getMileage();
//			}}, 1000, 200);
		}
	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		updateTimer.cancel();
//		unbindService(connection);
//	}

	public void catchException(Exception e){
		Log.wtf("Tim", "Something is broken");
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
		edit_box();
	}

	/*
	 *  Gets mileage from torque service
	 */
	public void getMileage(){
		String[] ids = new String[]{MILEAGE};
		try {
			float[] pidValues = torqueService.getPIDValues(ids);
			Log.i("Tim", "Mileage: "+pidValues[1]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			catchException(e);
		}
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
			super(PluginActivity.this, c);
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
	/**
	 * Bits of service code. You usually won't need to change this.
	 */
	private ServiceConnection connection = new ServiceConnection() {
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			torqueService = ITorqueService.Stub.asInterface(service);
		};
		public void onServiceDisconnected(ComponentName name) {
			torqueService = null;
		};
	};
}
