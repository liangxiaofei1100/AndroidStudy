package com.example.alex.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.StackView;

import com.example.alex.R;

public class StackViewActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack_view);
        
        final StackView stackView = (StackView) findViewById(R.id.mStackView);
        
        ColorAdapter colorAdapter = new ColorAdapter(this, mColors);
        stackView.setAdapter(colorAdapter);
        
        final Button previousButon = (Button) findViewById(R.id.previousButton);
        previousButon.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				stackView.showPrevious();
			}
		});
        
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				stackView.showNext();
			}
		});
    }
    
    private int [] mColors = {Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.RED};
    
}