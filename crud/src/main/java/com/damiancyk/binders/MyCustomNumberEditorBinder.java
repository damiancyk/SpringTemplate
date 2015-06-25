package com.damiancyk.binders;

import java.text.NumberFormat;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

public class MyCustomNumberEditorBinder extends CustomNumberEditor {

	public MyCustomNumberEditorBinder(Class<? extends Number> numberClass, NumberFormat numberFormat)
			throws IllegalArgumentException {
		super(numberClass, numberFormat, true);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text != null && text.length() > 0) {
			super.setAsText(text.trim().replaceAll(",", "."));
		} else {
			// setValue(0);
		}
	}

}