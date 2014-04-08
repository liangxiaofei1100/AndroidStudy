package com.example.alex.wifi;

import com.example.alex.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WifiDirectServerDiscoryDemo extends Activity {
	private TextView mTextView;
	WiFiDirectService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		mService = new WiFiDirectService(getApplicationContext(),
				getMainLooper());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Registery");
		menu.add(0, 2, 2, "Dicovery");
		menu.add(0, 3, 3, "Search peer.");
		menu.add(0, 4, 4, "Stop Search peer.");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			// Registery
			mService.startRegistration();
			break;
		case 2:
			// Dicovery
			mService.discorerService();
			break;
		case 3:
			// search peer
			mService.searchPeer();
		case 4:
			// stop search peer
			mService.stopSearchPeer();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
