package com.example.alex.animation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.R;

@SuppressLint("NewApi")
public class ImageAnimation extends Activity {

	ViewGroup mSceneRootView;
	Scene mScene1;
	Scene mScene2;
	int mCurrentScene = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_animation);
		mSceneRootView = (ViewGroup) findViewById(R.id.ll_scene_root);

		mScene1 = Scene.getSceneForLayout(mSceneRootView,
				R.layout.image_animation_scene1, this);
		mScene2 = Scene.getSceneForLayout(mSceneRootView,
				R.layout.image_animation_scene2, this);

	}

	/**
	 * Onclick
	 * 
	 * @param view
	 */
	public void showAnimation(View view) {
		if (mCurrentScene == 1) {
			TransitionManager.go(mScene2, getTransition());
			mCurrentScene = 2;
		} else if (mCurrentScene == 2) {
			TransitionManager.go(mScene1, getTransition());
			mCurrentScene = 1;
		}
	}

	private Transition getTransition() {
		ChangeBounds changeBounds = new ChangeBounds();
		changeBounds.setDuration(200);
		
		Fade fadeOut = new Fade(Fade.OUT);
		fadeOut.setDuration(1000);
		Fade fadeIn = new Fade(Fade.IN);
		fadeIn.setDuration(1000);
		TransitionSet transition = new TransitionSet();
		transition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
		transition.addTransition(fadeOut).addTransition(changeBounds)
				.addTransition(fadeIn);
		return changeBounds;
	}
}
