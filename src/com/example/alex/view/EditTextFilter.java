package com.example.alex.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.R;

public class EditTextFilter extends Activity {
	private static final String TAG = "EditTextFilter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittext_filter);
		EditText editText = (EditText) findViewById(R.id.editText_editText_filter);
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10)});

	}

	private class EditTextLengthFilter implements InputFilter {
		private Toast mExceedMessageSizeToast;
		private int mMaxLength;

		public EditTextLengthFilter(int maxLength, Context context) {
			mMaxLength = maxLength;
			mExceedMessageSizeToast = Toast.makeText(context, "Size litmited: "
					+ maxLength, Toast.LENGTH_SHORT);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			int keep = mMaxLength - (dest.length() - (dend - dstart));
			if (keep < (end - start)) {
				mExceedMessageSizeToast.show();
			}

			if (keep <= 0) {
				return "";
			} else if (keep >= end - start) {
				return null; // keep original
			} else {
				return source.subSequence(start, start + keep);
			}
		}

	}
}
