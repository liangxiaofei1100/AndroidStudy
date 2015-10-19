package com.example.alex.animation;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.example.alex.R;
import com.example.alex.common.Log;


public class PaoWuXian extends Activity {

    View mEndView;
    View mAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paowuxian);
        mAnimationView = findViewById(R.id.iv_icon);
        mEndView = findViewById(R.id.tv_end);
    }

    private static final int ANIMATION_DURATION_MS = 800;
    private static final int ANIMATION_TOP_Y_DP = 50;

    public void onStartButtonClicked(final View view) {
        int[] locationStart = new int[2];
        mAnimationView.getLocationInWindow(locationStart);
        int[] locationEnd = new int[2];
        mEndView.getLocationInWindow(locationEnd);
        float xDelta = locationEnd[0] - locationStart[0] + mEndView.getWidth() / 2 - mAnimationView.getWidth() / 2;
        float yDelta = locationEnd[1] - locationStart[1] - mAnimationView.getHeight();
        showAnimation(mAnimationView, xDelta, yDelta);
    }

    private void showAnimation(final View view, final float xDelta, final float yDelta) {
        AnimationSet animationSet1 = new AnimationSet(false);
        Animation animation1 = new TranslateAnimation(0, xDelta * 0.3f, 0, 0);
        animation1.setInterpolator(new LinearInterpolator());
        animation1.setDuration((long) (ANIMATION_DURATION_MS * 0.3));
        Animation animation2 = new TranslateAnimation(0, 0, 0, -200);
        animation2.setInterpolator(new DecelerateInterpolator(1.0f));
        animation2.setDuration((long) (ANIMATION_DURATION_MS * 0.3));
        animationSet1.addAnimation(animation1);
        animationSet1.addAnimation(animation2);

        animationSet1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showAnimationStage2();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            private void showAnimationStage2() {
                AnimationSet animationSet2 = new AnimationSet(false);
                Animation animation1 = new TranslateAnimation(xDelta * 0.3f, xDelta, 0, 0);
                animation1.setInterpolator(new LinearInterpolator());
                animation1.setDuration((long) (ANIMATION_DURATION_MS * 0.7));
                Animation animation2 = new TranslateAnimation(0, 0, -200, yDelta);
                animation2.setInterpolator(new AccelerateInterpolator(1.0f));
                animation2.setDuration((long) (ANIMATION_DURATION_MS * 0.7));

                animationSet2.addAnimation(animation1);
                animationSet2.addAnimation(animation2);
                animationSet2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animationSet2.setFillAfter(true);
                view.startAnimation(animationSet2);
            }
        });
        view.startAnimation(animationSet1);
    }


//    {
//        final ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setObjectValues(new PointF(mAnimationView.getX(), mAnimationView.getY()),
//                new PointF(mEndView.getX(), mEndView.getY()));
//        Log.d(pointFToString(new PointF(mAnimationView.getX(), mAnimationView.getY())));
//        Log.d(pointFToString(new PointF(mEndView.getX(), mEndView.getY())));
//
//        valueAnimator.setDuration(ANIMATION_DURATION_MS);
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        TypeEvaluator evaluator = new TypeEvaluator<PointF>() {
//
//            // fraction = t / duration
//            @Override
//            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
//                Log.d("start: " + pointFToString(startValue) + ", end: " + pointFToString(endValue));
//                // x方向匀速运动 ，则y方向先减速，后加速
//                PointF point = new PointF();
//                point.x = startValue.x + (endValue.x - startValue.x) * fraction;
//                if (fraction < 0.3) {
//                    point.y = startValue.y + (-10) * fraction + 50 * fraction * fraction;
//                } else {
//
//                }
//
//                Log.d("fraction=" + fraction + pointFToString(point));
//                return point;
//            }
//        };
//        valueAnimator.setEvaluator(evaluator);
//
//        valueAnimator.start();
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                PointF point = (PointF) animation.getAnimatedValue();
//                mAnimationView.setX(point.x);
//                mAnimationView.setY(point.y);
//                Log.d(pointFToString(point));
//            }
//        });
//    }

    public static String pointFToString(PointF pointF) {
        return "(" + pointF.x + "," + pointF.y + ")";
    }

    /**
     * 抛物线
     */
    public void paowuxian(View view) {

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();

            }
        });
    }
}
