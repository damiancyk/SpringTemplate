package com.damiancyk.decorator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.damiancyk.utils.AjaxUtils;

public class DecoratorUtils {

	public static String getJson(List<?> rows, Class<?> decoratorClass)
			throws Exception {
		ArrayList<Object> resultList = getObjects(rows, decoratorClass);
		return AjaxUtils.toJson(resultList);
	}

	public static String getJsonAngular(List<?> rows, Class<?> decoratorClass,
			Long counter) throws Exception {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("counter", counter);

		ArrayList<Object> resultList = getObjects(rows, decoratorClass);
		data.put("rows", resultList);

		return AjaxUtils.toJson(data);
	}

	public static ArrayList<Object> getObjects(List<?> rows,
			Class<?> decoratorClass) throws Exception {
		try {
			ArrayList<Object> resultList = new ArrayList<Object>();
			if (rows == null || rows.size() < 1) {
				return resultList;
			}

			Object firstRow = rows.get(0);
			Class<? extends Object> rowClass = firstRow.getClass();

			for (Object row : rows) {
				try {
					Constructor<?> constructor = decoratorClass
							.getDeclaredConstructor(rowClass);
					DecoratorTable decorator = (DecoratorTable) constructor
							.newInstance(row);
					resultList.add(decorator);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getObjects.error");
		}
	}

}
