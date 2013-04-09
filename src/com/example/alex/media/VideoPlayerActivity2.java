package com.example.alex.media;

import java.io.File;

import com.example.alex.R;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity2 extends Activity{
	private VideoView mVideoView;
	private int mCurrentPosition;
	private boolean hasPaused = false;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide titlebar of application
		// must be before setting the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide statusbar of Android
		// could also be done later
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.video_player2);
		mVideoView = (VideoView)findViewById(R.id.videoView);

		if (!new File(MediaUtil.VIDEO_FILE).exists()) {
			Toast.makeText(this, "video file not exist: " + MediaUtil.VIDEO_FILE,
					Toast.LENGTH_SHORT).show();
			finish();
		}
        /*
         * Alternatively,for streaming media you can use
         * mVideoView.setVideoURI(Uri.parse(URLstring));
         */
        mVideoView.setVideoPath(MediaUtil.VIDEO_FILE);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		hasPaused = true;
		mCurrentPosition = mVideoView.getCurrentPosition();
		mVideoView.suspend();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (hasPaused) {
			mVideoView.seekTo(mCurrentPosition);
			mVideoView.resume();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
