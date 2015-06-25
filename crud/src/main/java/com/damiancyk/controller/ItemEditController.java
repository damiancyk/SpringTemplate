package com.damiancyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.damiancyk.beans.ItemBean;
import com.damiancyk.beans.Operator;
import com.damiancyk.forms.ItemForm;
import com.damiancyk.persistence.ItemPersistence;

@Controller
public class ItemEditController {

	@Autowired
	Operator operator;

	@Autowired
	ItemPersistence itemPersistence;

	@RequestMapping(value = "itemEdit-{id}", method = RequestMethod.GET)
	public String get(ModelMap map, RedirectAttributes redirectAttributes,
			@PathVariable("id") Long id) {
		try {
			ItemBean item = itemPersistence.findById(id);
			ItemForm form = new ItemForm(item);

			map.put("item", form);

			return "itemAdd";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "edit.error");
			return "redirect:itemView";
		}
	}

	@RequestMapping(value = "itemEdit-{id}", method = RequestMethod.POST)
	public String post(ModelMap map, RedirectAttributes redirectAttributes,
			@ModelAttribute("item") ItemForm form, BindingResult result,
			@PathVariable("id") Long id) {
		try {
			form.validate(result);

			if (result.hasErrors()) {

				return "itemAdd";
			}

			itemPersistence.edit(form, id);
			redirectAttributes.addFlashAttribute("success", "edit.success");

			return "redirect:itemView";
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", "edit.error");
			return "itemAdd";
		}
	}

}
