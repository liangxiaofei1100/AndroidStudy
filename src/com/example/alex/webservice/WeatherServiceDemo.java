package com.example.alex.webservice;

import java.io.UnsupportedEncodingException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.alex.common.TimeUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WeatherServiceDemo extends Activity {
    private static final String LOG_TAG = "WeatherService";

    // 名空间
    private static final String NAMESPACE = "http://WebXml.com.cn/";
    // 网址
    private static String URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";
    // 方法名
    private static final String METHOD_NAME = "getWeather";
    // SOAPACTION
    private static String SOAP_ACTION = "http://WebXml.com.cn/getWeather";

    private String weatherToday;
    private SoapObject detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWeather("上海");
    }

    public void getWeather(String cityName) {
        Log.d(LOG_TAG, TimeUtil.getCurrentTime() + ": get weather " + cityName);
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("theCityName", cityName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(SOAP_ACTION, envelope);

            Log.d(LOG_TAG, "DUMP>> " + ht.requestDump);
            Log.d(LOG_TAG, "DUMP<< " + ht.responseDump);

            SoapObject result = (SoapObject) envelope.bodyIn;
            detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");

            parseWeather(detail);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseWeather(SoapObject detail) throws UnsupportedEncodingException {
        String date = detail.getProperty(6).toString();
        weatherToday = "今天：" + date.split(" ")[0];
        weatherToday = weatherToday + " 天气：" + date.split(" ")[1];
        weatherToday = weatherToday + " 气温：" + detail.getProperty(5).toString();
        weatherToday = weatherToday + " 风力：" + detail.getProperty(7).toString() + " ";
        Log.d(LOG_TAG, TimeUtil.getCurrentTime() + ": weatherToday is " + weatherToday);
        Toast.makeText(WeatherServiceDemo.this, weatherToday, Toast.LENGTH_LONG).show();
    }
}
