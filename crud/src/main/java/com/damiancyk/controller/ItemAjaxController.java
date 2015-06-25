package com.damiancyk.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.damiancyk.beans.ItemBean;
import com.damiancyk.beans.Operator;
import com.damiancyk.beans.TableAjaxParamBean;
import com.damiancyk.decorator.DecoratorUtils;
import com.damiancyk.decorator.ItemDecorator;
import com.damiancyk.persistence.ItemPersistence;

@Controller
public class ItemAjaxController {

	@Autowired
	Operator operator;

	@Autowired
	ItemPersistence itemPersistence;

	@RequestMapping(value = "itemView.json", method = RequestMethod.GET)
	public @ResponseBody
	String getJson(@ModelAttribute("tableParams") TableAjaxParamBean params)
			throws Exception {
		try {
			Long idCompany = operator.getIdCompany();

			Long count = itemPersistence.countViewByIds(params, idCompany);
			ArrayList<ItemBean> list = itemPersistence.findViewByIds(params,
					idCompany);

			String json = DecoratorUtils.getJsonAngular(list,
					ItemDecorator.class, count);

			return json;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
}
