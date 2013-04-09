package com.example.alex.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alex.R;
import com.example.alex.view.ImageTextButton;

public class TranslateActivity extends TranslateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mID = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_activity);
        initView();
    }

    ImageTextButton button1;
    private final static int STATE_1 = 1;
    private final static int STATE_2 = 2;
    int mCurrentState;

    private void initView() {
        button1 = (ImageTextButton) findViewById(R.id.btn_translate_activity1);
        ImageTextButton button2 = (ImageTextButton) findViewById(R.id.btn_translate_activity2);
        ImageTextButton button3 = (ImageTextButton) findViewById(R.id.btn_translate_activity3);
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);
        mCurrentState = STATE_1;
        updateButtonStatus();

        MyOnClickListener onClickListener = new MyOnClickListener();
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);

        TextView textView = (TextView) findViewById(R.id.tv_translate_activity);
        textView.setText("ID = " + mID);

        View view = findViewById(R.id.relative_layout_translate_activity);
        view.setBackgroundColor(Color.BLUE);

    }

    private void updateButtonStatus() {
        switch (mCurrentState) {
        case STATE_1:
            button1.setImageResource(R.drawable.tab_dial_ic_open);
            break;
        case STATE_2:
            button1.setImageResource(R.drawable.tab_dial_ic_close);
            break;

        default:
            break;
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_translate_activity1:
                mCurrentState = mCurrentState == STATE_1 ? STATE_2 : STATE_1;
                updateButtonStatus();
                break;
            case R.id.btn_translate_activity2:
                startActivity(TranslateActivity.this, TranslateActivity2.class);
                break;
            case R.id.btn_translate_activity3:
                startActivity(TranslateActivity.this, TranslateActivity3.class);
                break;
            default:
                break;
            }

        }
    }
}
