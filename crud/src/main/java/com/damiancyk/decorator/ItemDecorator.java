package com.damiancyk.decorator;

import com.damiancyk.beans.ItemBean;

public class ItemDecorator extends DecoratorTable {

	transient ItemBean bean;

	String nameDecorated;
	String createDateDecorated;

	public ItemDecorator(ItemBean bean) {
		this.nameDecorated = getLinkPopup("itemInfo-" + bean.getIdItem(),
				bean.getName());
		this.createDateDecorated = getDateDecorated(bean.getCreateDate());
	}

}
