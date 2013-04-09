package com.example.alex.gps;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * </br> GPS usage sample. </br> <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
public class GPSTestActivity extends Activity {
	private static final String TAG = "GPSTestActivity";

	private LocationManager mLocationManager;
	private LocationListener mLocationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new GpsLocationListener();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, mLocationListener);

		Location lastLocation = getLastLocation();
		if (lastLocation != null) {
			getAddressInfo(lastLocation);
		}
		
	}

	private String getProvider(LocationManager locationManager) {
		Criteria criteria = new Criteria();
		// �����õĶ�λЧ��
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // ����Ϊ��󾫶�
		criteria.setAltitudeRequired(false); // ����ȡ������Ϣ
		criteria.setBearingRequired(false); // ����ȡ��λ��Ϣ
		criteria.setCostAllowed(false); // �Ƿ�������
		criteria.setPowerRequirement(Criteria.POWER_LOW); // ʹ��ʡ��ģʽ
		// ��õ�ǰ��λ���ṩ��
		return locationManager.getBestProvider(criteria, true);
	}

	private Location getLastLocation() {
		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// location.getAccuracy(); ����
		// location.getAltitude(); �߶� : ����
		// location.getBearing(); ����
		// location.getSpeed(); �ٶ�
		// location.getLatitude(); γ��
		// location.getLongitude(); ����
		// location.getTime(); UTCʱ�� �Ժ����
		return location;
	}

	private void getAddressInfo(Location location) {
		Geocoder gc = new Geocoder(this);
		List<Address> addresses = null;
		try {
			// ���ݾ�γ�Ȼ�õ�ַ��Ϣ
			addresses = gc.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses.size() > 0) {
			// ��ȡaddress��ĳ�Ա��Ϣ
			String msg = "";
			msg += "AddressLine��" + addresses.get(0).getAddressLine(0) + "\n";
			msg += "CountryName��" + addresses.get(0).getCountryName() + "\n";
			msg += "Locality��" + addresses.get(0).getLocality() + "\n";
			msg += "FeatureName��" + addresses.get(0).getFeatureName();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mLocationListener);
	}
}
