package com.damiancyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.damiancyk.beans.ItemBean;
import com.damiancyk.beans.Operator;
import com.damiancyk.persistence.ItemPersistence;

@Controller
public class ItemInfoController {

	@Autowired
	Operator operator;

	@Autowired
	ItemPersistence itemPersistence;

	@RequestMapping(value = "itemInfo-{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") Long id, ModelMap map,
			RedirectAttributes redirectAttributes) {
		try {
			ItemBean item = itemPersistence.findById(id);
			map.put("item", item);

			return "itemInfo";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "info.error");

			return "itemView";
		}
	}

}
