package com.damiancyk.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityUtils {

	public final static String ENCODING = "UTF-8";

	public static String mergeTemplateIntoString(VelocityEngine velocityEngine,
			String fileName, ModelMap map, ApplicationContext context)
			throws Exception {
		try {
			String html = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "com/meestgroup/velocity/" + fileName,
					ENCODING, map);

			html = setFmtMessage(html, context);

			return html;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("mergeTemplateIntoString.error");
		}
	}

	public static String mergeDocumentTemplateIntoString(
			VelocityEngine velocityEngine, ModelMap map,
			ApplicationContext context) throws Exception {
		try {
			map.put("DateUtils", DateUtils.class);
			map.put("Utils", Utils.class);

			putCommonObjects(map);

			String fileName = "brandedDocument.vm";

			String html = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "com/meestgroup/velocity/" + fileName,
					ENCODING, map);

			html = setBoolFieldValuesWysiwygStyle(html, map);
			// html = setFmtMessage(html, context);
			// html = setFmtMessageWysiwygStyle(html, context, map);
			// html = setFieldValuesJspStyle(html, map);
			html = setFieldValuesWysiwygStyle(html, map);
			html = setBarCodeCN23(html, map);
			html = setBarCodeCP71(html, map);
			html = setLoopByTable(html, map);

			return html;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("mergeTemplateIntoString.error");
		}
	}

	public static String mergeDocumentTemplateBeforeIntoString(
			VelocityEngine velocityEngine, ModelMap map,
			ApplicationContext context) throws Exception {
		try {
			map.put("DateUtils", DateUtils.class);
			map.put("Utils", Utils.class);

			putCommonObjects(map);
			String fileName = "brandedDocumentBefore.vm";

			String html = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "com/meestgroup/velocity/" + fileName,
					ENCODING, map);

			return html;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("mergeTemplateIntoString.error");
		}
	}

	public static String mergeDocumentTemplateAfterIntoString(
			VelocityEngine velocityEngine, ModelMap map,
			ApplicationContext context) throws Exception {
		try {
			map.put("DateUtils", DateUtils.class);
			map.put("Utils", Utils.class);

			putCommonObjects(map);
			String fileName = "brandedDocumentAfter.vm";

			String html = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "com/meestgroup/velocity/" + fileName,
					ENCODING, map);

			return html;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("mergeTemplateIntoString.error");
		}
	}

	public static void putCommonObjects(ModelMap map) {
		Date dateNow = new Date();

		map.put("dateNow", DateUtils.dateToStringyyyMMdd(dateNow));
		map.put("dateTimeNow", DateUtils.dateToStringyyyMMddHHmm(dateNow));
		map.put("timeNow", DateUtils.dateToStringHHmm(dateNow));

	}

	private static String setFieldValueByClass(String html, ModelMap map,
			String divClass) {
		if (html == null || divClass == null) {
			return "";
		}

		Document doc = Jsoup.parse(html);
		Elements divs = doc.select("div." + divClass);
		for (Element div : divs) {
			Map<String, String> divData = div.dataset();
			String path = divData.get("field");
			String fieldValue = getFieldValueFromPath(path, map);
			div.after(fieldValue);
			div.remove();
		}

		String html2 = doc.html();
		return html;
	}

	private static String setLoopByTable(String html, ModelMap map)
			throws Exception {
		try {
			Document document = Jsoup.parse(html);
			// Elements tables = document.select("table.templateLoop");
			// for (Element table : tables) {

			Elements rowLoops = document.select("tr.loopContent");
			for (Element rowLoop : rowLoops) {
				try {
					ArrayList<Object> positions = (ArrayList<Object>) getFieldFromWordPair(
							"document", "documentPositions", map);

					if (positions != null) {
						int lp = 1;
						for (Object position : positions) {
							Element nextRow = rowLoop.clone();
							Elements divs = nextRow
									.select("div.templateLoopField");
							for (Element div : divs) {
								Map<String, String> data = div.dataset();
								String field = data.get("field");
								String fieldValue = null;
								if ("lp".equals(field)) {
									fieldValue = "" + lp;
								} else {
									Object fieldObj = getFieldFromObject(
											position, field, map);
									if (fieldObj != null) {
										if (fieldObj instanceof Double) {
											Double fieldDbl = (Double) fieldObj;
											fieldValue = Utils.float2(fieldDbl);
										} else {
											fieldValue = "" + fieldObj;
										}
									}
								}
								if (fieldValue != null) {
									div.after("<span class='replacedFieldLoop'>"
											+ fieldValue + "</span>");
								}
								div.remove();
							}

							String rowHtml = nextRow.html();

							rowLoop.after(rowHtml);
							// table.append(rowHtml);
							lp++;
						}

						rowLoop.remove();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// }

			return document.html();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("setLoop.error");
		}
	}

	@Deprecated
	private static String setFmtMessage(String html, ApplicationContext context) {
		Pattern pattern = Pattern.compile("<fmt:message key=\"(.*)\" />");
		Matcher matcher = pattern.matcher(html);
		Map<String, String> words = new HashMap<String, String>();

		while (matcher.find()) {
			String word = matcher.group(1);
			word = word.replace("\"", "");

			String wordLang = "";
			try {
				wordLang = context.getMessage(word, null, Locale.getDefault());
			} catch (Exception e) {
				e.printStackTrace();
				wordLang = "??" + word + "??";
			}

			words.put(word, wordLang);
		}

		for (Entry<String, String> word : words.entrySet()) {
			String key = word.getKey();
			String value = word.getValue();

			String patternReplace = "<fmt:message key=\"" + key + "\" />";
			html = html.replace(patternReplace, decorateReplacedValue(value));

		}

		return html;
	}

	@Deprecated
	private static String setFmtMessageWysiwygStyle(String html,
			ApplicationContext context, ModelMap map) {
		Pattern pattern = Pattern
				.compile("<div class=\"templateLang(.*)</div>");
		Matcher matcher = pattern.matcher(html);

		while (matcher.find()) {
			String divContent = matcher.group(1);

			String path = findBetweenStrings(divContent, "data-key=\"", "\"");

			Locale locale = null;
			String lang = null;

			// String lang1 = Utils.paramToString(map.get("lang1"));
			// String lang2 = Utils.paramToString(map.get("lang2"));

			if (divContent.contains("lang1")) {
				lang = Utils.getString(map.get("lang1"));
			} else if (divContent.contains("lang2")) {
				lang = Utils.getString(map.get("lang2"));
			}

			if (lang != null) {
				locale = new Locale(lang);
			} else {
				locale = Locale.getDefault();
			}

			String fieldValue = "";
			try {
				fieldValue = context.getMessage(path, null, locale);
			} catch (Exception e) {
				e.printStackTrace();
				fieldValue = "??" + path + "??";
			}

			String patternReplace = "<div class=\"templateLang" + divContent
					+ "</div>";

			html = html.replace(patternReplace, fieldValue);
		}

		return html;
	}

	@Deprecated
	private static String setFieldValuesJspStyle(String html, ModelMap map) {

		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(html);

		while (matcher.find()) {
			String path = matcher.group(0);
			path = path.replace("\"", "");
			path = path.substring(2, path.length() - 1);

			String fieldValue = getFieldValueFromPath(path, map);

			String patternReplace = "${" + path + "}";

			html = html.replace(patternReplace,
					decorateReplacedValue(fieldValue));
		}

		return html;
	}

	private static String setFieldValuesWysiwygStyleByClass(String html,
			ModelMap map, String divClass) {
		Document document = Jsoup.parse(html);
		Elements divs = document.select("div." + divClass);
		for (Element div : divs) {
			String path = div.dataset().get("field");
			String fieldValue = getFieldValueFromPath(path, map);

			if (fieldValue != null) {
				div.after("<span class='replacedField'>" + fieldValue
						+ "</span>");
			}
			div.remove();
		}

		return document.html();
	}

	private static String setFieldValuesWysiwygStyle(String html, ModelMap map) {
		String divClass = "templateField";
		return setFieldValuesWysiwygStyleByClass(html, map, divClass);
	}

	private static String setBoolFieldValuesWysiwygStyle(String html,
			ModelMap map) {
		Document document = Jsoup.parse(html);
		Elements elements = document.select("div.templateBoolField");
		for (Element element : elements) {
			Map<String, String> inputData = element.dataset();
			String path = inputData.get("field");
			String fieldValue = getFieldValueFromPath(path, map);

			String insertValue = "";
			if ("true".equals(fieldValue)) {
				insertValue = "X";
				// element.after("&#x2713;");
				// boolBlock.append("&#x2713;");
				element.html("<span style='vertical-align:middle; font-size:20px; font-weight: bold;'>X</span>");
			} else {
				element.html("");
			}

			// element.remove();
		}

		return document.html();
	}

	private static String setBarCodeCN23(String html, ModelMap map)
			throws Exception {
		try {
			Document document = Jsoup.parse(html);
			Elements blocks = document.select("div.templateBarCodeCN23");
			for (Element block : blocks) {
				Element img = block.select("img").first();
				Map<String, String> data = block.dataset();

				String number = ""
						+ getFieldFromWordPair("document", "barCode", map);
				String type = "code128";

				data.put("type", type);
				data.put("number", number);
			}

			return document.html();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("setBarCodeCN23.error");
		}
	}

	private static String setBarCodeCP71(String html, ModelMap map)
			throws Exception {
		try {
			Document document = Jsoup.parse(html);
			Elements blocks = document.select("div.templateBarCodeCP71");
			for (Element block : blocks) {
				Element img = block.select("img").first();
				Map<String, String> data = block.dataset();

				String number = ""
						+ getFieldFromWordPair("document", "barCode", map);
				String type = "code128";

				data.put("type", type);
				data.put("number", number);
			}

			return document.html();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("setBarCodeCP71.error");
		}
	}

	private static String decorateReplacedValue(String value) {
		value = "<strong>" + value + "</strong>";

		return value;
	}

	private static String findBetweenStrings(String text, String left,
			String right) {
		String result = "";

		ArrayList<String> results = findAllBetweenStrings(text, left, right);
		if (results != null && results.size() >= 1) {
			result = results.get(0);
		}

		return result;
	}

	private static ArrayList<String> findAllBetweenStrings(String text,
			String left, String right) {
		ArrayList<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile(left + "(.*)" + right);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String result = matcher.group(1);
			results.add(result);
		}

		return results;
	}

	private static String getFieldValueFromPath(String path, ModelMap map) {
		try {
			String[] words = path.split("\\.");

			if (words.length < 1) {
				return "";
			} else if (words.length == 1) {
				Object object = getObject(words[0], map);
				return "" + (object != null ? object : "");
			}

			String fieldObject = words[0];
			String fieldName = words[1];
			Object nextObject = getFieldFromWordPair(fieldObject, fieldName,
					map);

			for (int i = 2; i < words.length; i++) {
				fieldName = words[i];
				nextObject = getFieldFromObject(nextObject, fieldName, map);
			}

			String fieldValue = "";
			if (nextObject != null) {
				if (nextObject instanceof Double) {
					Double nextObjectDbl = (Double) nextObject;
					fieldValue = Utils.float2(nextObjectDbl);
				} else {
					fieldValue = "" + nextObject;
				}
			}

			return fieldValue;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static Object getObject(String objectStr, ModelMap map)
			throws Exception {
		try {
			Object object = map.get(objectStr);

			return object;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getObject.error");
		}
	}

	private static Object getFieldFromObject(Object object, String fieldStr,
			ModelMap map) throws Exception {
		try {
			Class<?> clazz = object.getClass();

			Field field = clazz.getDeclaredField(fieldStr);
			field.setAccessible(true);
			Object fieldObject = field.get(object);

			return fieldObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getFieldFromObject.error");
		}
	}

	private static Object getFieldFromWordPair(String objectStr,
			String fieldStr, ModelMap map) throws Exception {
		try {
			Object object = map.get(objectStr);
			Class<?> clazz = object.getClass();

			Field field = clazz.getDeclaredField(fieldStr);
			field.setAccessible(true);
			Object fieldObject = field.get(object);

			return fieldObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getFieldFromWordPair.error");
		}

	}

	public static void main(String[] args) throws Exception {
		setLoopByTable(null, null);

		// jsonGenerate();
		// String str = "5 * x^3 - 6 * x^1 + 1";
		// System.out.println("str: " + str);
		// str = str.replaceAll("\\s\\*\\s", "");
		// str = str.replaceAll("(\\^)(\\d)", "<sup>$2</sup>");
		// System.out.println("strRegExp: " + str);

		// String html = "jakiś tekst ${document.documentNumber} inny tekst";
		// Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		// Matcher matcher = pattern.matcher(html);
		//
		// while (matcher.find()) {
		// String word = matcher.group(0);
		// word = word.replace("\"", "");
		// word = word.substring(2, word.length() - 1);
		// String[] split = word.split("\\.");
		// String fieldObject = split[0];
		// String fieldName = split[1];
		// System.out.println(fieldObject);
		// }

		// setFmtMessage("<fmt:message key=\"required.field\"/>", null);

		// String html =
		// "jakiś tekst <div class=\"templateField\"><input data-field=\"document.documentNumber\"></div> jeszcze inny tekst";
		// html = setFieldValuesWysiwygStyle(html, null);
		// System.out.println("setFieldValuesWysiwygStyle:: " + html);
	}
}
