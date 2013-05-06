package com.example.alex.gps;

import java.io.IOException;
import java.util.List;

import com.example.alex.R;
import com.example.alex.common.ShowResultUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

/**
 * </br> GPS usage sample. </br> <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
public class GPSTestActivity extends Activity {
	private static final String TAG = "GPSTestActivity";

	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private ShowResultUtil mShowResultUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mShowResultUtil = new ShowResultUtil(mTextView);
		mLocationListener = new GpsLocationListener(mShowResultUtil);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check GPS is enabled or not.
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			ShowResultUtil.showInTextView(mTextView, "GPS is disabled.");
			openGPSSettings();
		} else {
			ShowResultUtil.showInTextView(mTextView, "GPS is enabled.");
		}
		String GPSProvider = GPSUtils.getProvider(mLocationManager);
		mShowResultUtil.showInTextView("getGPSProvider = " + GPSProvider);
		mLocationManager.requestLocationUpdates(GPSProvider,
				1000, 10, mLocationListener);

		Location lastLocation = getLastLocation();
		if (lastLocation != null) {
			mShowResultUtil.showInTextView(GPSUtils.getLocationInfo(lastLocation));
			mShowResultUtil.showInTextView(GPSUtils.getAddressInfo(lastLocation, this));
		}
	}

	private TextView mTextView;

	private void initView() {
		setContentView(R.layout.gps_test_activity);
		mTextView = (TextView) findViewById(R.id.tv_gps_test_result);
	}

	private Location getLastLocation() {
		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		return location;
	}

	

	private void openGPSSettings() {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}
}
