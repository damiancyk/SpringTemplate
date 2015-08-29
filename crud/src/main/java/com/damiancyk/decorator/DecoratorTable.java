package com.damiancyk.decorator;

import java.util.Date;
import java.util.Locale;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import com.damiancyk.interfaces.EnumLangInterface;
import com.damiancyk.utils.DateUtils;
import com.damiancyk.utils.SpringContextUtils;

public abstract class DecoratorTable {

	public static String getMessageFromKey(String key) {
		StringBuilder lang = new StringBuilder();
		if (key == null || key.length() == 0) {
			return lang.toString();
		}

		ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
		Locale locale = LocaleContextHolder.getLocale();

		try {
			lang.append(applicationContext.getMessage(key, null, locale));
		} catch (Exception e) {
			e.printStackTrace();
			lang.append("???");
			lang.append(key);
			lang.append("???");
		}

		return lang.toString();
	}

	public static String getMessageFromKey(EnumLangInterface enumLang) {
		if (enumLang == null) {
			return "";
		}

		String key = enumLang.getText();
		if (key == null) {
			return "";
		}

		ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
		if (applicationContext == null) {
			return "";
		}

		return getMessageFromKey(key);
	}

	public static String getCheckbox(Object id) {
		StringBuilder sb = new StringBuilder();

		sb.append("<input type='checkbox' class='check' style='display:none'");
		sb.append(" data-id-position='" + id + "'");
		sb.append(" />");

		return sb.toString();
	}

	@Deprecated
	public static String getCheckbox(String link, Object id) {
		StringBuilder sb = new StringBuilder();

		sb.append("<input type='checkbox' class='check' style='display:none'");
		sb.append(" data-id-position='" + id + "'");
		sb.append(" data-link='" + link + "-" + id + ".html'");
		sb.append(" />");

		return sb.toString();
	}

	public static String getIcon(String path) {
		if (path == null) {
			path = "img/empty_logo.png";
		}

		String str = "<img src='" + path + "' style='width:40px;height:40px;'></img>";

		return str;
	}

	public static String getLink(String url, String text) {
		if (url == null || text == null) {
			return "";
		} else if (url.contains("-null")) {
			return text;
		}

		String str = "<a href='" + url + "'>" + text + "</a>";

		return str;
	}

	public static String getLinkNewTab(String url, String text) {
		if (url == null || text == null) {
			return "";
		}

		String str = "<a href='" + url + "' target='_blank' >" + text + "</a>";

		return str;
	}

	public static String getLinkDownload(String fileRealPath, String text) {
		if (fileRealPath == null || fileRealPath.length() == 0 || text == null || text.length() == 0) {
			return "";
		}

		String str = "<a href='downloadSimpleFile/?fileRealPath=" + fileRealPath
				+ "&openType=down&fileType=document' target='_new' class='pure-button pure-button-xsmall' >" + text
				+ "</a>";

		return str;
	}

	public static String getLinkPopup(String url, String text) {
		if (url == null || text == null) {
			return "";
		} else if (url.contains("-null")) {
			return text;
		}

		String str = "<a href='" + url + "' onclick='invokeDialog(undefined, event)'>" + text + "</a>";

		return str;
	}

	public static String getShortString(String str, int max) {
		if (str.length() <= max) {
			return str;
		}

		StringBuilder s = new StringBuilder();
		s.append(str.substring(0, max));
		s.append("...");

		return s.toString();
	}

	public static String getDateDecorated(Date date) {
		return DateUtils.dateToStringyyyMMdd(date);
	}

	public static String getDateDecoratedWithTime(Date date) {
		return DateUtils.dateToStringyyyMMddHHmm(date);
	}

	public static String getBoolean(Boolean flag) {
		if (flag != null && flag == true) {
			return getMessageFromKey("form.yes");
		} else {
			return getMessageFromKey("form.no");
		}
	}

	// private class TableGeneric {
	//
	// }

	public static void dynamicFields() {
		try {
			CtClass classTabeGen = ClassPool.getDefault().makeClass("TableGen");

			// CtClass point =
			// ClassPool.getDefault().get("com.meestgroup.decorators.TableGeneric");
			// CtMethod m =
			// CtNewMethod.make("public int xmove(int dx) { x += dx; }",
			// classTabeGen);
			// classTabeGen.addMethod(m);

			CtField f = new CtField(CtClass.intType, "z", classTabeGen);
			classTabeGen.addField(f);

			CtConstructor constructor = new CtConstructor(null, null);
			classTabeGen.addConstructor(constructor);

			Class<? extends CtClass> class1 = classTabeGen.getClass();
			CtConstructor[] constructors = classTabeGen.getDeclaredConstructors();
			System.out.println(constructors[0]);
			Object o = new Object();

			// ProductViewBean product = new ProductViewBean();
			// Class<? extends Object> rowClass = product.getClass();
			// classTabeGen.getClass().getDeclaredConstructor(rowClass);

			// String json = AjaxUtils.toJson(class1);
			// System.out.println("json:: " + json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// dynamicFields();
	}

}
