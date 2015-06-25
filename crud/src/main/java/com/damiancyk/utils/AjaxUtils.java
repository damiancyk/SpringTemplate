package com.damiancyk.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import com.damiancyk.interfaces.EnumLangInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AjaxUtils {

	public static String AJAX_ERROR_MSG = "ERROR";

	public static String toJson(Object o) {
		String json = toJsonGson(o);

		return json;
	}

	public static String toJsonGson(Object o) {
		String json = "";

		if (o != null) {
			final GsonBuilder builder = new GsonBuilder();
			builder.disableInnerClassSerialization();

			final Gson gson = builder.create();

			json = gson.toJson(o);
		}

		return json;
	}

	public static String toJsonJackson(Object o) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(o);

			return json;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("toJsonJackson.error");
		}
	}

	public static <T> Object fromJson(String json, Class<T> clazz) {
		if (json == null || json.trim().length() == 0) {
			return null;
		}

		T o = new Gson().fromJson(json, clazz);

		return o;
	}

	// TODO powinno przyjmowac klase enuma a nie konkretna wartosc..
	public static String getEnumJson(Enum<? extends EnumLangInterface> en) {
		Class<? extends EnumLangInterface> declaringClass = en
				.getDeclaringClass();
		HashMap<String, String> values = new HashMap<String, String>();

		ApplicationContext applicationContext = SpringContextUtils
				.getApplicationContext();
		Locale locale = LocaleContextHolder.getLocale();

		for (EnumLangInterface constant : en.getDeclaringClass()
				.getEnumConstants()) {
			String text = constant.getText();
			String lang = null;
			try {
				lang = applicationContext.getMessage(text, null, locale);
			} catch (Exception e) {
				StringBuilder langErr = new StringBuilder();
				langErr.append("??");
				langErr.append(text);
				langErr.append("??");
				lang = langErr.toString();
			}

			values.put("" + constant, lang);
		}
		ValueComparator vc = new ValueComparator(values);
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(vc);
		sortedMap.putAll(values);
		Gson gson = new Gson();
		String json = gson.toJson(sortedMap);

		return json;
	}

	public static void putEnumJson(Class<? extends Enum> clazz) {
		// enumClass.getDeclaringClass().gete
	}

	public static <T extends Enum & EnumLangInterface> void myMethod(T param) {

	}

	// public static double roundToDecimals(double d, int c) {
	//
	// if (d == 0)
	// return 0;
	//
	// BigDecimal bd = new BigDecimal(d);
	// bd = bd.setScale(c, BigDecimal.ROUND_UP);
	// d = bd.doubleValue();
	// return d;
	// }
	//
	// public static double roundToDecimals(double d) {
	// return roundToDecimals(d, 2);
	// }
	//
	// public static void main(String[] args) {
	// System.out.println("round 486.99 " + roundToDecimals(486.99, 2));
	// System.out.println("round 33.333333 " + roundToDecimals(33.333333, 2));
	// }

	public static <T extends Enum<?> & EnumLangInterface> void spew(T t) {
		// t.getDeclaringClass()
		// t.append("Bleah!\n");
		// if (timeToClose())
		// t.close();
	}

	public static void main(String[] args) {
		// myMethod(InvoiceTypeEnum.class);
	}

}

class ValueComparator implements Comparator<String> {

	Map<String, String> map;

	public ValueComparator(Map<String, String> base) {
		this.map = base;
	}

	public int compare(String a, String b) {
		if (map.get(a) != null) {
			return map.get(a).compareTo(map.get(b));
		} else {
			return -1;
		}
	}
}