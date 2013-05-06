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
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置为最大精度
		criteria.setAltitudeRequired(true); // 不获取海拔信息
		criteria.setBearingRequired(false); // 不获取方位信息
		criteria.setCostAllowed(true); // 是否允许付费
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 使用省电模式
		// 获得当前的位置提供者
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
			// 根据经纬度获得地址信息
			addresses = gc.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses.size() > 0) {
			// 获取address类的成员信息
			addressInfo += "AddressLine：" + addresses.get(0).getAddressLine(0)
					+ "\n";
			addressInfo += "CountryName：" + addresses.get(0).getCountryName()
					+ "\n";
			addressInfo += "Locality：" + addresses.get(0).getLocality() + "\n";
			addressInfo += "FeatureName：" + addresses.get(0).getFeatureName();
		}
		return addressInfo;
	}

}
