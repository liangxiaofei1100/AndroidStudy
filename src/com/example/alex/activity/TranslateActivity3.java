package com.example.alex.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alex.R;
import com.example.alex.view.ImageTextButton;

public class TranslateActivity3 extends TranslateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mID = 3;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_activity);
        initView();
    }

    private void initView() {
        ImageTextButton button1 = (ImageTextButton) findViewById(R.id.btn_translate_activity1);
        ImageTextButton button2 = (ImageTextButton) findViewById(R.id.btn_translate_activity2);
        ImageTextButton button3 = (ImageTextButton) findViewById(R.id.btn_translate_activity3);

        button1.setSelected(false);
        button2.setSelected(false);
        button3.setSelected(true);
        
        MyOnClickListener onClickListener = new MyOnClickListener();
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);

        TextView textView = (TextView) findViewById(R.id.tv_translate_activity);
        textView.setText("ID = " + mID);

        View view = findViewById(R.id.relative_layout_translate_activity);
        view.setBackgroundColor(Color.YELLOW);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_translate_activity1:
                startActivity(TranslateActivity3.this, TranslateActivity.class);
                break;
            case R.id.btn_translate_activity2:
                startActivity(TranslateActivity3.this, TranslateActivity2.class);
                break;
            case R.id.btn_translate_activity3:
                // do nothing
                break;
            default:
                break;
            }

        }

    }
}
