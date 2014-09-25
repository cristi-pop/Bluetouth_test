package com.example.bluetouthtest;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public TextView statusUpdate;
	public Button connect;
	public Button disconnect;
	public ImageView logo;
	private BluetoothAdapter btAdapter;
	
	BroadcastReceiver bluetoothState = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
			String stateExtra = BluetoothAdapter.EXTRA_STATE;
			int state = intent.getIntExtra(stateExtra, -1);
			int previousState = intent.getIntExtra(prevStateExtra, -1);
			String toastText = "";
			switch(state){
				case(BluetoothAdapter.STATE_TURNING_ON):
				{
					toastText = "Bluetooth turning on";			
					Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
					break;
				}
				case(BluetoothAdapter.STATE_ON):
				{
					toastText = "Bluetooth on";	
					Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
					setupUI();
					break;
				}
				case(BluetoothAdapter.STATE_TURNING_OFF):
				{
					toastText = "Bluetooth turning off";	
					Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
					break;
				}
				case(BluetoothAdapter.STATE_OFF):
				{
					toastText = "Bluetooth off";
					Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
					setupUI();
					break;
				}
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
	}

	private void setupUI() {
		statusUpdate = (TextView) findViewById(R.id.textViewStatus);
		connect = (Button) findViewById(R.id.buttonConnect);
		disconnect = (Button) findViewById(R.id.buttonDisconnect);
		logo = (ImageView) findViewById(R.id.imageViewLogo);
		
		disconnect.setVisibility(View.GONE);
		logo.setVisibility(View.GONE);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter.isEnabled()){
			String address = btAdapter.getAddress();
			String name = btAdapter.getName();
			String statusText = name + " : " + address;
			statusUpdate.setText(statusText);
			
			connect.setVisibility(View.GONE);
			disconnect.setVisibility(View.VISIBLE);
			logo.setVisibility(View.VISIBLE);
		}
		else{
			connect.setVisibility(View.VISIBLE);
			statusUpdate.setText("Bluetooth is not on");
		}
		
		connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
				String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
				IntentFilter filter = new IntentFilter(actionStateChanged);
				registerReceiver(bluetoothState, filter);
				startActivityForResult(new Intent(actionRequestEnable), 0);
			/*	
				String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
				String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
				IntentFilter filter = new IntentFilter(actionStateChanged);
				registerReceiver(bluetoothState, filter);
				startActivityForResult(new Intent(actionRequestEnable), 0);*/
			}
		});
		
		disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btAdapter.disable();
				connect.setVisibility(View.VISIBLE);
				disconnect.setVisibility(View.GONE);
				logo.setVisibility(View.GONE);
				statusUpdate.setText("Bluetooth Off");
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
