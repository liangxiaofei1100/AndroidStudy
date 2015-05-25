package com.example.alex.view;

import com.example.alex.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DragView extends View {
	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public static final String Tag = "CreatView";
	private Context mContext;
	protected static DragView mCreatView;
	private int[] location;
	private ImageView mImageView;
	public WindowManager mWm;
	public WindowManager.LayoutParams layoutParams;

	public DragView(Context context) {
		super(context);
		setClickable(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.mContext = context;
		mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		layoutParams = new WindowManager.LayoutParams();
	}

	public synchronized static DragView getCreatView(Context context) {
		Log.d(Tag, "getCreatView");
		if (mCreatView == null) {
			mCreatView = new DragView(context);
		}
		return mCreatView;
	}

	public void setLocation(int[] location) {
		this.location = location;
	}

	private View createNewView() {
		Log.d(Tag, "createNewView");
		ImageView mImageView = new ImageView(mContext);
		mImageView.setImageDrawable(mContext.getResources().getDrawable(
				R.drawable.ic_launcher));
		mImageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
		return mImageView;
	}

	public void addViewToScreen() {
		Log.d(Tag, "addViewToScreen");
		mImageView = (ImageView) createNewView();
		mImageView.setOnTouchListener(new TouchListener());
		layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		layoutParams.x = location[0] + 30;
		layoutParams.y = location[1];
		layoutParams.width = 40;
		layoutParams.height = 40;
		layoutParams.alpha = 1.0f;
		mWm.addView(mImageView, layoutParams);
	}

	class TouchListener implements OnTouchListener {
		int lastX;
		int lastY;
		int screenWidth;
		int screenHeight;

		public TouchListener() {
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
			Log.d(Tag, "screen width =" + screenWidth + ",screen height="
					+ screenHeight);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d(Tag, "TouchListener -- onTouch");
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				Log.d(Tag, "down x=" + lastX + ", y=" + lastY);
				break;
			case MotionEvent.ACTION_MOVE:
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;
				Log.d(Tag, "move dx=" + dx + ",  dy=" + dy);
				int left = v.getLeft() + dx;
				int top = v.getTop() + dy;
				int right = v.getRight() + dx;
				int bottom = v.getBottom() + dy;
				Log.d(Tag, "view  left=" + left + ", top=" + top + ", right="
						+ right + ",bottom=" + bottom);
				// set bound
				if (left < 0) {
					left = 0;
					right = left + v.getWidth();
				}
				if (right > screenWidth) {
					right = screenWidth;
					left = right - v.getWidth();
				}
				if (top < 0) {
					top = 0;
					bottom = top + v.getHeight();
				}
				if (bottom > screenHeight) {
					bottom = screenHeight;
					top = bottom - v.getHeight();
				}
				v.layout(left, top, right, bottom);
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				layoutParams.x = lastX;
				layoutParams.y = lastY - 30;
				mWm.updateViewLayout(v, layoutParams);

				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		}
	}

	Paint paint = new Paint();
	Matrix matrix = new Matrix();
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.ic_launcher);

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, matrix, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			mDragViewMoveListener.onDragViewMove(this, event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return super.onTouchEvent(event);

	}
	private OnDragViewMoveListener mDragViewMoveListener;
	public void setOnDragViewMoveListener(OnDragViewMoveListener listener) {
		mDragViewMoveListener = listener;
	}
	public interface OnDragViewMoveListener {
		void onDragViewMove(DragView view, float x, float y);
	}
}
