package com.example.alex.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return format.format(date);
	}
}
