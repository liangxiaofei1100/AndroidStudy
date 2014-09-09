package com.example.alex.animation.petal;

import java.util.ArrayList;

public class PetalThread extends Thread {
	private boolean mIsRun;
	// Change this can change petal speed.
	private static final int SLEEP_TIME = 30;
	// Change this can change petal number.
	private static final int PETAL_ADD_TIME = 40;
	private PetalView mPetalView;
	private int mTime;

	public PetalThread(PetalView pv) {
		this.mPetalView = pv;
		this.mIsRun = true;
	}

	public void quit() {
		mIsRun = false;
	}

	public void run() {
		long startTime = 0;
		while (mIsRun) {
			startTime = System.currentTimeMillis();
			// Add a petal
			if (mTime % PETAL_ADD_TIME == 0) {
				mPetalView.mPetalSet.add(1, mPetalView.getWidth(),
						mPetalView.getHeight());
				mTime = 0;
			}
			mTime++;
			// Move all petal
			ArrayList<Petal> petals = mPetalView.mPetalSet.petalSet;
			for (int i = 0; i < petals.size(); i++) {
				Petal petal = petals.get(i);
				int x = (int) (petal.x + petal.acceleration_x);
				int y = (int) (petal.y + petal.acceleration_y);

				if (y > mPetalView.getHeight() || x > mPetalView.getWidth()) {
					petals.remove(i);
				} else {
					petal.x = x;
					petal.y = y;
				}
			}
			// Delay
			try {
				Thread.sleep(Math.max(0,
						SLEEP_TIME - (System.currentTimeMillis() - startTime)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}