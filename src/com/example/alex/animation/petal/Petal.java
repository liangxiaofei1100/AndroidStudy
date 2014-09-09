package com.example.alex.animation.petal;

import android.graphics.Bitmap;

public class Petal {
	Bitmap image;
	int imageAlpha;
	int x;
	int y;
	int startX;
	int startY;
	double acceleration_x;
	double acceleration_y;

	public Petal(Bitmap image, int imageAlpha, int x, int y,
			double acceleration_x, double acceleration_y) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.acceleration_y = acceleration_y;
		this.acceleration_x = acceleration_x;
		this.imageAlpha = imageAlpha;
	}

}
