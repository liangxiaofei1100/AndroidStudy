package com.example.alex.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LightSenorTestActivity extends Activity implements
		SensorEventListener {
	private static final String TAG = "LightSenorTestActivity";
	private Sensor sensor;
	private TextView mTextView;
	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTextView = new TextView(this);
		setContentView(mTextView);

		mTextView.setText("Light Senor Test:\n");
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this, sensor);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.i(TAG, "onAccuracyChanged");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		for (int i = 0; i < event.values.length; i++) {
			mTextView.append("light senor value changed: value[" + i + "]: "
					+ event.values[i]+"\n");
		}
	}

}
