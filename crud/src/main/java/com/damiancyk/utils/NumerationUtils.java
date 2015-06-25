package com.damiancyk.utils;

import java.util.Calendar;
import java.util.Date;

public class NumerationUtils {

	public static String words[] = { "rok", "miesiac", "numer" };

	public static String parse(String config, Long number, Date createDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createDate);

		String numberStr = String.valueOf(number);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() < 2) {
			month = "0" + month;
		}

		String parsedString = config.replaceAll("\\[" + words[0] + "\\]", year)
				.replaceAll("\\[" + words[1] + "\\]", month)
				.replaceAll("\\[" + words[2] + "\\]", numberStr);

		return parsedString;
	}

}
