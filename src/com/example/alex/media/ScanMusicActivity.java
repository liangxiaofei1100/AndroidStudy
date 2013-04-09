package com.example.alex.media;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import com.example.alex.R;

public class ScanMusicActivity extends Activity {
	private Context mContext;
	private TextView mResulTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mContext = this;
		
		mResulTextView = (TextView) findViewById(R.id.tv_show_result);
		// show all audio info.
		mResulTextView.append("External Audio before scan:\n");
		mResulTextView.append(InsertAudioIntoPlaylist.showAllAudioInfo(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, this));
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		registerReceiver(mMediaScanReciever, intentFilter);
		
		scanFileAsync(this, "/mnt/sdcard/test.mp3");
	};
	
	private BroadcastReceiver mMediaScanReciever = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(intent.getAction())) {
				mResulTextView.append("Media scan started.\n");
			} else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(intent.getAction())) {
				mResulTextView.append("Media scan finished.\n");
				// show all audio info.
				mResulTextView.append("External Audio before scan:\n");
				mResulTextView.append(InsertAudioIntoPlaylist.showAllAudioInfo(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mContext));
			}
		}
	};
	public void scanFileAsync(Context ctx, String filePath) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(new File(filePath)));
		ctx.sendBroadcast(scanIntent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mMediaScanReciever);
	}
	
	private void getAudioFolder() {
		
	}
	
}
