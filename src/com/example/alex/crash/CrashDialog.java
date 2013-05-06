package com.example.alex.crash;

import com.example.alex.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class CrashDialog extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		show(this);
	}
	public static void show(final Context context) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("Crashed")
				.setMessage("Your application has crashed again.")
				.setPositiveButton(android.R.string.ok, null).create();
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

}
