package com.example.alex.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.alex.R;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class OpendGLESBeginning extends Activity {
	MySurfaceView mSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_opengles_beginning);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSurfaceView.onPause();
	}

	private void initView() {
		mSurfaceView = new MySurfaceView(this);
		mSurfaceView.requestFocus();
		mSurfaceView.setFocusableInTouchMode(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_game_opengles_beginning);
		layout.addView(mSurfaceView);
	}

}

class MySurfaceView extends GLSurfaceView {
	private final static float TOUCH_SCALE_FACTOR = 180 / 320;
	private SceneRender mSceneRender;
	private float mPreviousX;
	private float mPreviousY;

	public MySurfaceView(Context context) {
		super(context);
		mSceneRender = new SceneRender();
		setRenderer(mSceneRender);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			mSceneRender.mTriangle.setYAngle(mSceneRender.mTriangle.getYAngle()
					+ dx * TOUCH_SCALE_FACTOR);
			mSceneRender.mTriangle.setZAngle(mSceneRender.mTriangle.getZAngle()
					+ dy * TOUCH_SCALE_FACTOR);
			requestRender();
			break;

		default:
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}

	private class SceneRender implements GLSurfaceView.Renderer {
		Triangle mTriangle;

		public SceneRender() {
			mTriangle = new Triangle();
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glFrontFace(GL10.GL_CCW);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glTranslatef(0, 0, -2.0f);

			mTriangle.drawSelf(gl);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}

}
