package com.example.alex.gps;

import com.example.alex.common.ShowResultUtil;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GpsLocationListener implements LocationListener {
	private static final String TAG = "GpsLocationListener";

	private ShowResultUtil mShowResultUtil;

	public GpsLocationListener(ShowResultUtil showResultUtil) {
		mShowResultUtil = showResultUtil;
		Log.d(TAG, "GpsLocationListener");
	}

	@Override
	public void onLocationChanged(Location location) {
		mShowResultUtil.showInTextView(GPSUtils.getLocationInfo(location));
		Log.d(TAG, "onLocationChanged.");
		Log.d(TAG, getLocationInfo(location));
	}

	@Override
	public void onProviderDisabled(String provider) {
		mShowResultUtil.showInTextView("onProviderDisabled: " + provider);
		Log.d(TAG, "onProviderDisabled: " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		mShowResultUtil.showInTextView("onProviderEnabled: " + provider);
		Log.d(TAG, "onProviderEnabled: " + provider);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		mShowResultUtil.showInTextView("onStatusChanged: " + "provider: "
				+ provider + "\nstatus: " + status);
		Log.d(TAG, "onStatusChanged.");
		Log.d(TAG, "provider: " + provider);
		Log.d(TAG, "status: " + status);

	}

	private String getLocationInfo(Location location) {
		if (location == null) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Accuracy: " + location.getAccuracy() + "\n");
		stringBuilder.append("Altitude: " + location.getAltitude() + "\n");
		stringBuilder.append("Bearing: " + location.getBearing() + "\n");
		stringBuilder.append("Speed: " + location.getSpeed() + "\n");
		stringBuilder.append("Latitude: " + location.getLatitude() + "\n");
		stringBuilder.append("Longitude: " + location.getLongitude() + "\n");
		stringBuilder.append("Time: " + location.getTime() + "\n");
		return stringBuilder.toString();
	}

}
