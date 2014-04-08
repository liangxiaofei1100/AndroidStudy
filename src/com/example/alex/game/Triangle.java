package com.example.alex.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Triangle {
	private static final String TAG = "Triangle";
	private final int UNIT_SIZE = 10000;
	private final int mVertexCount = 3;

	private IntBuffer mVertexBuffer;
	private IntBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
	private int mIndexCount = 0;
	private float mYAngle = 0;
	private float mZAngle = 0;

	public Triangle() {
		int[] vertices = new int[] { -8 * UNIT_SIZE, 6 * UNIT_SIZE, 0,
				-8 * UNIT_SIZE, -6 * UNIT_SIZE, 0, -8 * UNIT_SIZE,
				-6 * UNIT_SIZE, 0 };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asIntBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		// 支持65535 色色彩通道
		final int one = 65535;
		int[] colors = new int[] { one, one, one, 0, one, one, one, 0, one,
				one, one, 0, };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asIntBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);

		// 为三角形构造索引数据初始化
		mIndexCount = 3;
		byte[] indices = new byte[] { 0, 1, 2 };
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length * 4);
		mIndexBuffer.position(0);
	}

	public float getYAngle() {
		return mYAngle;
	}

	public void setYAngle(float angle) {
		mYAngle = angle;
	}

	public float getZAngle() {
		return mZAngle;
	}

	public void setZAngle(float angle) {
		mZAngle = angle;
	}

	public void drawSelf(GL10 gl) {
		Log.d(TAG, "drawSelf");
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glRotatef(mYAngle, 0, 1, 0);
		gl.glRotatef(mZAngle, 0, 0, 1);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, mVertexCount,
				GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
}
