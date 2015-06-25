package com.damiancyk.forms;

import org.springframework.validation.BindingResult;

import com.damiancyk.beans.ItemBean;
import com.damiancyk.enums.StatusEnum;
import com.damiancyk.utils.ValidationUtils;

public class ItemForm {

	private Long idItem;
	private String name;
	private StatusEnum status;

	public ItemForm() {
	}

	public ItemForm(ItemBean bean) {
		this.idItem = bean.getIdItem();
		this.name = bean.getName();
		this.status = bean.getStatus();
	}

	public void validate(BindingResult result) {
		ValidationUtils.notEmpty("name", name, result);
	}

	public Long getIdItem() {
		return idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}
