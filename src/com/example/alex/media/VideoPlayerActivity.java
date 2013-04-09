package com.example.alex.media;

import java.io.File;
import java.io.IOException;

import com.example.alex.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;

public class VideoPlayerActivity extends Activity implements
		SurfaceHolder.Callback, OnPreparedListener, OnErrorListener {
	private static final String TAG = "VideoPlayerActivity";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private MediaPlayer mMediaPlayer;
	private MediaController mMediaController;
	private boolean hasPaused = false;
	private int mVideoPosition = 0;

	private Uri mUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		final Intent intent = getIntent();
		mUri = intent.getData();
		final String scheme = intent.getScheme();
		final String type = intent.getType();
		if ("http".equalsIgnoreCase(scheme) || "rtsp".equalsIgnoreCase(scheme)) {
			if ("video/mp4".equalsIgnoreCase(type)
					|| "video/flv".equalsIgnoreCase(type)
					|| "video/x-flv".equalsIgnoreCase(type)) {

			}
		}
	}

	private void initView() {
		// hide titlebar of application must be before setting the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide statusbar of Android could also be done later
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_player);

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		mSurfaceView.getHolder().addCallback(this);
		mMediaController = (MediaController) findViewById(R.id.mediaController);
		mMediaController.setAnchorView(mSurfaceView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		hasPaused = true;
		mVideoPosition = mMediaPlayer.getCurrentPosition();
		Log.d(TAG, "on pause : position: " + mVideoPosition);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surface created.");
		mHolder = holder;

		try {
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
				if (mUri != null) {
					Log.d(TAG, "play network video");
					mMediaPlayer.setDataSource(this, mUri);
				} else {
					Log.d(TAG, "play local video");
					if (!new File(MediaUtil.VIDEO_FILE).exists()) {
						Toast.makeText(
								this,
								"video file not exist: " + MediaUtil.VIDEO_FILE,
								Toast.LENGTH_SHORT).show();
						finish();
					}

					mMediaPlayer.setDataSource(MediaUtil.VIDEO_FILE);
				}
				mMediaPlayer.setOnPreparedListener(this);
				mMediaPlayer.setOnErrorListener(this);
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setScreenOnWhilePlaying(true);
			}
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surface destroyed.");
		mHolder = null;
		releaseMediaPlayer();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mMediaPlayer.start();
		if (hasPaused) {
			Log.d(TAG, "on prepared, position: " + mVideoPosition);
			mMediaPlayer.seekTo(mVideoPosition);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		releaseMediaPlayer();
		return true;
	}
}
