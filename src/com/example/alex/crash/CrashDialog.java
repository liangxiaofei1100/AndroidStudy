package com.example.alex.crash;

import com.example.alex.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CrashDialog extends DialogFragment {

	public static CrashDialog getInstance(String log) {
		CrashDialog crashDialog = new CrashDialog();
		
		Bundle args = new Bundle();
		args.putString("log", log);
		crashDialog.setArguments(args);

		return crashDialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String log = getArguments().getString("log");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.crash_dialog, container, false);
		return view;
	}

}
