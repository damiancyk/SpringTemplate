package com.damiancyk.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginForm {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String password;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
