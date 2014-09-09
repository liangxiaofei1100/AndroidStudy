package com.example.alex.animation.petal;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
	private PetalView mPetalView;
	private SurfaceHolder mSurfaceHolder;
	boolean mIsRun;
	private static final int FPS = 90;
	private static final int SLEEP_TIME = 1000 / FPS;

	public DrawThread(PetalView pv, SurfaceHolder sHolder) {
		this.mSurfaceHolder = sHolder;
		this.mIsRun = true;
		this.mPetalView = pv;
	}

	public void stopDraw() {
		mIsRun = false;
	}

	public void run() {
		long startTime = 0;
		Canvas canvas = null;
		while (mIsRun) {
			startTime = System.currentTimeMillis();
			try {
				canvas = mSurfaceHolder.lockCanvas(null);

				synchronized (mSurfaceHolder) {
					mPetalView.doDraw(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

			try {
				Thread.sleep(Math.max(0,
						SLEEP_TIME - (System.currentTimeMillis() - startTime)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
