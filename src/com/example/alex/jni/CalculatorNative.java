package com.example.alex.jni;

public class CalculatorNative {

	static {
		System.loadLibrary("CalculatorJNI");
	}

	native public static int add(int a, int b);
}
