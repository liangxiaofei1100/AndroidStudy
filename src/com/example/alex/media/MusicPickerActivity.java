package com.example.alex.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;

public class MusicPickerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		startActivity(Intent.createChooser(intent, "Select music"));

		
	}
	
	private void auidoManager() {
		AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		manager.requestAudioFocus(new OnAudioFocusChangeListener() {

			@Override
			public void onAudioFocusChange(int focusChange) {
				// AUDIOFOCUS_GAIN, AUDIOFOCUS_LOSS, AUDIOFOCUS_LOSS_TRANSIENT
				// and AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK.
				switch (focusChange) {
				case AudioManager.AUDIOFOCUS_GAIN:

					break;

				default:
					break;
				}

			}
		}, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}
}
