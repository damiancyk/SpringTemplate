package com.damiancyk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.damiancyk.forms.LoginForm;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(ModelMap map, HttpServletRequest request,
			@RequestParam(value = "login", required = false) String login,
			@RequestParam(value = "password", required = false) String password) {

		if (request.getParameter("error") != null) {
			map.put("error", request.getParameter("error"));
		}

		if (request.getParameter("logoutSuccess") != null) {
			map.put("logoutSuccess", "login.logout.success");
		}

		LoginForm form = new LoginForm();
		form.setUserName(login);
		form.setPassword(password);

		map.put("loginForm", form);

		return "login";

	}
}