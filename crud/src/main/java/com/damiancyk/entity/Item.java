package com.damiancyk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.damiancyk.enums.StatusEnum;

@Entity
@Table(name = "ITEM")
public class Item {

	private Long idItem;
	private String name;
	private StatusEnum status;
	private Date createDate;

	public Item() {
	}

	public Item(Long idItem) {
		this.idItem = idItem;
	}

	@Id
	@SequenceGenerator(name = "item_seq_gen", sequenceName = "ITEM_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "item_seq_gen")
	@Column(name = "ID_ITEM", nullable = false, precision = 22, scale = 0)
	public Long getIdItem() {
		return idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20)
	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}