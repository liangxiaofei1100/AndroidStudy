package com.example.alex.sensor;

import com.example.alex.common.SimpleTestActivity;
import com.example.alex.common.TimeUtil;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LightSenorTestActivity extends SimpleTestActivity implements
		SensorEventListener {
	private static final String TAG = "LightSenorTestActivity";
	private Sensor sensor;
	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInputEditText.setVisibility(View.GONE);
		mShowResultButton.setVisibility(View.GONE);

		mResulTextView.setText("Light Senor Test\n");
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
		mResulTextView.append(TimeUtil.getCurrentTime() + " value:"
				+ event.values[0] + "\n");
		scrollToBottom();
	}

}
