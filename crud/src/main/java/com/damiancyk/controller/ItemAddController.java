package com.damiancyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.damiancyk.beans.Operator;
import com.damiancyk.forms.ItemForm;
import com.damiancyk.persistence.ItemPersistence;

@Controller
public class ItemAddController {

	@Autowired
	Operator operator;

	@Autowired
	ItemPersistence itemPersistence;

	@RequestMapping(value = "itemAdd", method = RequestMethod.GET)
	public String get(ModelMap map, RedirectAttributes redirectAttributes) {
		try {
			ItemForm form = new ItemForm();

			map.put("item", form);

			return "itemAdd";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "add.error");
			return "redirect:itemView";
		}
	}

	@RequestMapping(value = "itemAdd", method = RequestMethod.POST)
	public String post(ModelMap map, RedirectAttributes redirectAttributes,
			@ModelAttribute("item") ItemForm form, BindingResult result) {
		try {
			form.validate(result);

			if (result.hasErrors()) {

				return "itemAdd";
			}

			itemPersistence.save(form);
			redirectAttributes.addFlashAttribute("success", "add.success");

			return "redirect:itemView";
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", "add.error");
			return "itemAdd";
		}
	}

}
