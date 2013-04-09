package com.example.alex.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.alex.R;

public class DragViewActivity extends Activity{
	public static final String Tag = "MainActivity";

	private Button creatorBtn;
	private ArrayList<DragView> mDragViews;
	private RelativeLayout mRelativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dragview_activity);
		mDragViews = new ArrayList<DragView>();

		mRelativeLayout = (RelativeLayout) findViewById(R.id.viewgroup_drag_view);
		creatorBtn = (Button) this.findViewById(R.id.btn_create_drag_view);
		creatorBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// createDragView(v);
				DragView dragView = DragView
						.getCreatView(DragViewActivity.this);
				addDragViewToLayout(dragView);

				mDragViews.add(dragView);
			}

		});
	}

	private void addDragViewToLayout(View view) {
		mRelativeLayout.addView(view);
	}

}