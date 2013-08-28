package com.sandz.helloworld;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SecondActivity extends Activity {
	
	private MaintenanceHelper dbMaintenanceHelper = null;
	private Cursor ourCursor = null;
	private MaintenanceAdapter adapter = null;
//	
//	ArrayList<String> maintList = new ArrayList<String>();
//	EditText myEditText1;
//	EditText myEditText2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_second);
//		ListView element, obtained by id
			ListView myListView = (ListView)findViewById(R.id.maintListView);
//		EditText Element
			myEditText1 = (EditText)findViewById(R.id.editMaintText);
			myEditText2 = (EditText)findViewById(R.id.editIinterval);
			
			dbMaintenanceHelper = new MaintenanceHelper(this);
			
			dbMaintenanceHelper.createDatabase();
			dbMaintenanceHelper.openDatabase();
			
			ourCursor = dbMaintenanceHelper.getCursor();
			
//		turn on array adapter
			adapter=new MaintenanceAdapter();
			myListView.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VideoString = http://www.youtube.com/watch?v=Awu7Rlsez_k&list=PLCdeOMca6qwtJ9AOGPBQYgLW1TZcWORz9&feature=player_detailpage#t=119
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}
	
	public void addNotification(View view){
//		add the note
		maintList.add(0, myEditText1.getText().toString());
//		update the view
		adapter.notifyDataSetChanged();
//		erase the text so we can add another note
		myEditText1.setText("");
		myEditText2.setText("");
	}

	public void removeNotification(View view){
		
	}
	
	class MaintenanceAdapter extends ArrayAdapter<String> {
		
		public MaintenanceAdapter() {
			super(SecondActivity.this, android.R.layout.simple_list_item_1, maintList);
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
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

	}

	static class MaintenanceHolder{
		public TextView note = null;

		MaintenanceHolder(View row){
			note = (TextView)row.findViewById(R.id.maintText);
		}
		void populateFrom(String r){
			note.setText(r);
		}
	}

}
