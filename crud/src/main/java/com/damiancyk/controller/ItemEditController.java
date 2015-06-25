package com.damiancyk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ItemEditController {

	@RequestMapping(value = "itemEdit-{id}", method = RequestMethod.GET)
	public String get() {
		try {

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "itemEdit";
	}

}
