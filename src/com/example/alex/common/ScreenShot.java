package com.example.alex.common;

import android.app.Activity;
import android.graphics.Bitmap;

public class ScreenShot {

    public static Bitmap takeScreenShot(Activity context) {
        context.getWindow().getDecorView().setDrawingCacheEnabled(true);
        return context.getWindow().getDecorView().getDrawingCache();
    }
}
