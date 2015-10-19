package com.example.alex.animation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.alex.R;
import com.example.alex.common.Log;

public class PraiseAnimation extends Activity {
    private ImageView mImageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_praise_animation);
        mImageView = (ImageView) findViewById(R.id.iv_praise);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d();
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.praise);
                mImageView.startAnimation(animation);
            }
        });
    }
}
