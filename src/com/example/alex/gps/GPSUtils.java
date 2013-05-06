package com.example.alex.gps;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class GPSUtils {
	public static String getProvider(LocationManager locationManager) {
		Criteria criteria = new Criteria();
		// �����õĶ�λЧ��
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // ����Ϊ��󾫶�
		criteria.setAltitudeRequired(true); // ����ȡ������Ϣ
		criteria.setBearingRequired(false); // ����ȡ��λ��Ϣ
		criteria.setCostAllowed(true); // �Ƿ�������
		criteria.setPowerRequirement(Criteria.POWER_LOW); // ʹ��ʡ��ģʽ
		// ��õ�ǰ��λ���ṩ��
		return locationManager.getBestProvider(criteria, true);
	}

	public static String getLocationInfo(Location location) {
		String locationInfo = "";
		if (location != null) {
			locationInfo += "Accuracy = " + location.getAccuracy() + '\n';
			locationInfo += "Altitude = " + location.getAltitude() + '\n';
			locationInfo += "Latitude = " + location.getLatitude() + '\n';
			locationInfo += "Longitude = " + location.getLongitude() + '\n';
			locationInfo += "Time = " + location.getTime() + '\n';
		}
		return locationInfo;
	}

	public static String getAddressInfo(Location location, Context context) {
		String addressInfo = null;

		Geocoder gc = new Geocoder(context);
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
			addressInfo += "AddressLine��" + addresses.get(0).getAddressLine(0)
					+ "\n";
			addressInfo += "CountryName��" + addresses.get(0).getCountryName()
					+ "\n";
			addressInfo += "Locality��" + addresses.get(0).getLocality() + "\n";
			addressInfo += "FeatureName��" + addresses.get(0).getFeatureName();
		}
		return addressInfo;
	}

}
