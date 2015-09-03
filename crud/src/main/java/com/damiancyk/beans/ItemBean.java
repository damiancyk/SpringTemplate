package com.damiancyk.beans;

import java.util.Date;

import com.damiancyk.entity.Item;
import com.damiancyk.enums.StatusEnum;

public class ItemBean {
/////
	private Long idItem;
	private String name;
	private StatusEnum status;
	private Date createDate;

	public ItemBean() {
	}

	public ItemBean(Item entity) {
		this.idItem = entity.getIdItem();
		this.name = entity.getName();
		this.status = entity.getStatus();
		this.createDate = entity.getCreateDate();
	}

	public ItemBean(Long idItem, String name, Date createDate) {
		super();
		this.idItem = idItem;
		this.name = name;
		this.createDate = createDate;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
