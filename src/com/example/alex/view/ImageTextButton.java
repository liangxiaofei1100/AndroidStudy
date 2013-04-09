package com.example.alex.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.R;

public class ImageTextButton extends LinearLayout {
    private ImageView mButtonImage = null;
    private TextView mButtonText = null;
    private View mDriver = null;

    private int imageNormal;
    private int imageSelected;

    private int textColorNormal;
    private int textColorSelected;
    
    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        final String text = typedArray.getString(R.styleable.ImageTextButton_text);
        final float textSize = typedArray.getDimension(R.styleable.ImageTextButton_textSize, 12);
        textColorNormal = typedArray.getColor(R.styleable.ImageTextButton_textColor, 0x999999);
        textColorSelected = typedArray.getColor(R.styleable.ImageTextButton_textColorSelected, 0xffffff);
        
        imageNormal = typedArray.getResourceId(R.styleable.ImageTextButton_image, 0);
        imageSelected = typedArray.getResourceId(R.styleable.ImageTextButton_imageSelected, 0);

        // set image view
        mButtonImage = new ImageView(context);
        setImageResource(imageNormal);
        mButtonImage.setPadding(0, 6, 0, 0);
        LayoutParams imageParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        imageParams.weight = 4;
        mButtonImage.setLayoutParams(imageParams);

        // set text view
        mButtonText = new TextView(context);
        mButtonText.setGravity(Gravity.CENTER);
        setText(text);
        setTextColor(textColorNormal);
        setTextSize(textSize);
        mButtonText.setPadding(0, 0, 0, 3);
        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        textParams.weight = 1;
        mButtonText.setLayoutParams(textParams);

        // set diver view. It is invisible default.
        mDriver = new View(context);
        mDriver.setBackgroundColor(Color.BLUE);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 3);
        mDriver.setLayoutParams(params);
        mDriver.setVisibility(View.INVISIBLE);

        // set layout
        setClickable(true);
        setFocusable(true);
        setOrientation(LinearLayout.VERTICAL);

        addView(mButtonImage);
        addView(mButtonText);
        addView(mDriver);

        typedArray.recycle();
    }

    public void setText(int resId) {
        mButtonText.setText(resId);
    }

    public void setText(CharSequence buttonText) {
        mButtonText.setText(buttonText);
    }

    public void setTextColor(int color) {
        mButtonText.setTextColor(color);
    }

    public void setTextSize(float size) {
        mButtonText.setTextSize(size);
    }

    public void setImageResource(int resId) {
        mButtonImage.setImageResource(resId);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            mDriver.setVisibility(View.VISIBLE);
            mButtonText.setTextColor(textColorSelected);
            mButtonImage.setImageResource(imageSelected);
        } else {
            mDriver.setVisibility(View.INVISIBLE);
            mButtonText.setTextColor(textColorNormal);
            mButtonImage.setImageResource(imageNormal);
        }
    }

}
