package com.xzedu.crm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 对Date格式疯转
 */
public class DateUtil {
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toSimpleDate(Date date) {
		return sdf1.format(date);
	}

	public static String toFullDate(Date date) {
		return sdf2.format(date);
	}
	public static String getNowFullDate() {
		return toFullDate(new Date());
	}
	public static String getNowSimpleDate() {
		return toSimpleDate(new Date());
	}
}
