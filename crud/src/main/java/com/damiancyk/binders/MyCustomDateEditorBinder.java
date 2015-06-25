package com.damiancyk.binders;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MyCustomDateEditorBinder extends PropertyEditorSupport {

	private List<DateFormat> parseFormatters;
	private boolean allowEmpty;

	public MyCustomDateEditorBinder(List<DateFormat> parseFormatters,
			boolean allowEmpty) {
		this.parseFormatters = parseFormatters;
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && StringUtils.isEmpty(text)) {
			setValue(null);
		} else if (text != null) {
			boolean parseable = false;
			List<String> errors = new ArrayList<String>();
			for (DateFormat formatter : this.parseFormatters) {

				try {
					Date date = formatter.parse(text);
					setValue(date);
					parseable = true;
					break;
				} catch (ParseException e) {
					errors.add(e.getMessage());
				}

			}

			if (!parseable) {
				throw new IllegalArgumentException("Unparseable string: "
						+ errors.toString());
			}
		}
	}

}