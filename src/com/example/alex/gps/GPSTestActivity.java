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
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置为最大精度
		criteria.setAltitudeRequired(false); // 不获取海拔信息
		criteria.setBearingRequired(false); // 不获取方位信息
		criteria.setCostAllowed(false); // 是否允许付费
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 使用省电模式
		// 获得当前的位置提供者
		return locationManager.getBestProvider(criteria, true);
	}

	private Location getLastLocation() {
		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// location.getAccuracy(); 精度
		// location.getAltitude(); 高度 : 海拔
		// location.getBearing(); 导向
		// location.getSpeed(); 速度
		// location.getLatitude(); 纬度
		// location.getLongitude(); 经度
		// location.getTime(); UTC时间 以毫秒计
		return location;
	}

	private void getAddressInfo(Location location) {
		Geocoder gc = new Geocoder(this);
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
			String msg = "";
			msg += "AddressLine：" + addresses.get(0).getAddressLine(0) + "\n";
			msg += "CountryName：" + addresses.get(0).getCountryName() + "\n";
			msg += "Locality：" + addresses.get(0).getLocality() + "\n";
			msg += "FeatureName：" + addresses.get(0).getFeatureName();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mLocationListener);
	}
}
