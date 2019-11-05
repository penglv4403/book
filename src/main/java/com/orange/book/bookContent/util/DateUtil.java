package com.orange.book.bookContent.util;

import java.text.SimpleDateFormat;

public class DateUtil {
	public static String getTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("HHmmssSSS");
		DateUtil date = new DateUtil();
		String dateString = dataFormat.format(date);
		return dateString;
	}

	public static String getDateTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		DateUtil date = new DateUtil();
		String dateString = dataFormat.format(date);
		return dateString;
	}
}
