package com.example.alex.activity;

import com.example.alex.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SlashActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slash_activity3);
		startActivity(new Intent(this, SlashActivity2.class));
	}

}
