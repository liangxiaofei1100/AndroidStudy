package com.example.alex.game;

import com.example.alex.R;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SoundPoolDemo extends Activity implements OnClickListener {
	private static final String TAG = "SoundPoolDemo";
	private SoundPool mSoundPool;
	private AudioManager mAudioManager;
	// Used for play sound
	private SparseIntArray mSoundPoolArray;
	// Used for pause sound
	private SparseIntArray mSoundPoolPlayingArray;
	// Sound ids
	private static final int SOUND_ID_1 = 1;
	private static final int SOUND_ID_2 = 2;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.game_soundpool);
		initSoundPool();
		initView();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_play_sound1:
			playSound(SOUND_ID_1);
			break;
		case R.id.btn_pause_sound1:
			pauseSound(SOUND_ID_1);
			break;
		case R.id.btn_play_sound2:
			playSound(SOUND_ID_2);
			break;
		case R.id.btn_pause_sound2:
			pauseSound(SOUND_ID_2);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSoundPool.release();
		mSoundPool = null;
	}

	private void initView() {
		Button playSound1 = (Button) findViewById(R.id.btn_play_sound1);
		playSound1.setOnClickListener(this);
		Button pauseSound1 = (Button) findViewById(R.id.btn_pause_sound1);
		pauseSound1.setOnClickListener(this);
		Button playSound2 = (Button) findViewById(R.id.btn_play_sound2);
		playSound2.setOnClickListener(this);
		Button pauseSound2 = (Button) findViewById(R.id.btn_pause_sound2);
		pauseSound2.setOnClickListener(this);
	}

	private void initSoundPool() {
		mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolArray = new SparseIntArray();
		mSoundPoolPlayingArray = new SparseIntArray();
		int sound1 = mSoundPool.load(mContext, R.raw.sound1, 1);
		mSoundPoolArray.put(SOUND_ID_1, sound1);
		mSoundPoolPlayingArray.put(SOUND_ID_1, sound1);
		int sound2 = mSoundPool.load(mContext, R.raw.sound2, 1);
		mSoundPoolArray.put(SOUND_ID_2, sound2);
		mSoundPoolPlayingArray.put(SOUND_ID_2, sound2);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	}

	private void playSound(int soundID) {
		float audioMaxVolumn = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float audioCurrentVolumn = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
		int id = mSoundPool.play(mSoundPoolArray.get(soundID), volumnRatio,
				volumnRatio, 1, 0, 1);
		Log.d(TAG, "playSound returned id = " + id);
		mSoundPoolPlayingArray.put(soundID, id);
	}

	private void pauseSound(int soundID) {
		mSoundPool.pause(mSoundPoolPlayingArray.get(soundID));
	}

}
