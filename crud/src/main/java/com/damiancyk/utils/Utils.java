package com.damiancyk.utils;

import java.text.NumberFormat;

public class Utils {

	public static String getString(Object param) {
		String str = "" + param;

		if ("null".equals(str)) {
			return null;
		}

		return str;
	}

	public static String floatVal(Double val, Integer minimumFractionDigits,
			Integer maximumFractionDigicts) {
		if (val == null) {
			return "";
		}

		NumberFormat formatter = NumberFormat.getInstance();
		if (minimumFractionDigits != null) {
			formatter.setMinimumFractionDigits(minimumFractionDigits);
		}
		if (maximumFractionDigicts != null) {
			formatter.setMaximumFractionDigits(maximumFractionDigicts);
		}
		formatter.setGroupingUsed(false);

		String str = formatter.format(val);

		return str;
	}

	public static String float0(Double val) {
		return floatVal(val, 0, null);
	}

	public static String float02(Double val) {
		return floatVal(val, 0, 2);
	}

	public static String float2(Double val) {
		return floatVal(val, 2, 2);
	}

	public static <T extends Comparable<T>> boolean checkCollision(T a, T b,
			T c, T d) {
		if (b != null && c != null && b.compareTo(c) < 0) {
			return false;
		} else if (a != null && d != null && d.compareTo(a) < 0) {
			return false;
		}

		return true;
	}

}
