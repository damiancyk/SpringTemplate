package com.damiancyk.utils;

import org.springframework.validation.BindingResult;

public class ValidationUtils {

	public static void notEmpty(String name, String value, BindingResult result) {
		if (value == null || value.length() == 0) {
			result.rejectValue(name, "required.field");
		}
	}
}
