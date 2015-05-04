package com.example.alex.tmp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.alex.R;

public class ScreenShotActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ImageView v = (ImageView) findViewById(R.id.iv_screenshot);
            View view = v.getRootView();

            Bitmap bitmap = createViewBitmap(view);

            v.setImageBitmap(bitmap);
            
            
            File file = new File(Environment.getExternalStorageDirectory() + "/screenshot.jpg");
            FileOutputStream outputStream = null;
            try {
                 outputStream = new FileOutputStream(file);
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally{
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            

            
        };
    };

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

}
