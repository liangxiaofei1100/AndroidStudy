package com.example.alex.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GpsLocationListener implements LocationListener {
	private static final String TAG = "GpsLocationListener";

	public GpsLocationListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged.");
		Log.d(TAG, getLocationInfo(location));
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled: " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled: " + provider);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
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
