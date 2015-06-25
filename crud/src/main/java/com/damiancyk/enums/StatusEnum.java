package com.damiancyk.enums;

import com.damiancyk.interfaces.EnumLangInterface;

public enum StatusEnum implements EnumLangInterface {

	ACTIVE("status.active"), DELETED("status.deleted"), BLOCKED(
			"status.blocked"), UNSIGNED("status.unsigned"), NOT_ACTIVATED(
			"status.not.activated"), DEACTIVATED("status.deactivated");

	private final String text;

	StatusEnum(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
