package com.example.alex.webservice;

import java.io.UnsupportedEncodingException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.alex.R;
import com.example.alex.common.TimeUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherServiceDemo extends Activity {
	private static final String LOG_TAG = "WeatherService";

	private static final String NAMESPACE = "http://WebXml.com.cn/";
	private static String URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";
	private static final String METHOD_NAME = "getRegionCountry";
	private static String SOAP_ACTION = "http://WebXml.com.cn/getRegionCountry";

	private String weatherToday;
	private SoapObject detail;

	private TextView mResultTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mResultTextView = (TextView) findViewById(R.id.tv_show_result);

		getWeatherAsync();
	}

	private final static int MSG_GET_WEATHER_START = 1;
	private final static int MSG_GET_WEATHER_FINISH = 2;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_GET_WEATHER_START:
				mResultTextView.append("get weather start\n");
				break;

			case MSG_GET_WEATHER_FINISH:
				mResultTextView.append("get weather end\n");
				mResultTextView.append("weather is : " + weatherToday);
				break;

			default:
				break;
			}
		};
	};

	private void getWeatherAsync() {
		Thread thread = new Thread() {

			public void run() {
				mHandler.sendEmptyMessage(MSG_GET_WEATHER_START);
				getWeather("�Ϻ�");
				mHandler.sendEmptyMessage(MSG_GET_WEATHER_FINISH);
			}
		};

		thread.start();
	}

	public void getWeather(String cityName) {
		Log.d(LOG_TAG, TimeUtil.getCurrentTime() + ": get weather " + cityName);
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("theCityName", cityName);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);

			Log.d(LOG_TAG, "DUMP>> " + ht.requestDump);
			Log.d(LOG_TAG, "DUMP<< " + ht.responseDump);

			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = (SoapObject) result
					.getProperty("getWeatherbyCityNameResult");

			parseWeather(detail);
			return;
		} catch (Exception e) {
			Log.e(LOG_TAG, "getWeather error: " + e.toString());
			e.printStackTrace();
		}
	}

	private void parseWeather(SoapObject detail)
			throws UnsupportedEncodingException {
		String date = detail.getProperty(6).toString();
		weatherToday = "���죺" + date.split(" ")[0];
		weatherToday = weatherToday + " ����" + date.split(" ")[1];
		weatherToday = weatherToday + " ���£�"
				+ detail.getProperty(5).toString();
		weatherToday = weatherToday + " ������"
				+ detail.getProperty(7).toString() + " ";
		Log.d(LOG_TAG, TimeUtil.getCurrentTime() + ": weatherToday is "
				+ weatherToday);
	}
}
