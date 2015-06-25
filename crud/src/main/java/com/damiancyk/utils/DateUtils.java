package com.damiancyk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String dateToString(Date date, String format) {
		if (date == null || format == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String dateToStringHHmm(Date date) {
		return dateToString(date, "HH:mm");
	}

	public static String dateToStringyyyMMdd(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	public static String dateToStringyyyMMddHHmm(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm");
	}

}
