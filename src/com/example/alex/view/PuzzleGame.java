package com.example.alex.view;

import com.example.alex.common.DisplayUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class PuzzleGame extends Activity{
	private static final String TAG = "PuzzleGame";
	private PuzzleView mPuzzleView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DisplayUtil.enableNoTitle(this);
		DisplayUtil.enableFullScreen(this);
		
		mPuzzleView = new PuzzleView(this);
		setContentView(mPuzzleView);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mPuzzleView.onTouchEvent(event)) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}
		
	}
}
