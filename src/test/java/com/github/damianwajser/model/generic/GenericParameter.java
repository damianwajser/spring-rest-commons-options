package com.github.damianwajser.model.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.github.damianwajser.annotations.Auditable;

public abstract class GenericParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6926077659789502636L;

	private Integer id;
	@NotBlank(message = "El campo code es obligatorio")
	private String code;
	protected Date createDate;
	protected Date modifyDate;
	protected String createUser;
	protected String modifyUser;

	private Long version;

	private boolean isActive;

	public Integer getId() {
		return id;
	}

	@Auditable
	public Date getCreateDate() {
		return createDate;
	}

	@Auditable
	public Date getModifyDate() {
		return modifyDate;
	}

	@Auditable
	public String getCreateUser() {
		return createUser;
	}

	@Auditable
	public String getModifyUser() {
		return modifyUser;
	}

	@JsonGetter("modifyDate")
	public String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		sdf.setLenient(false);
		return sdf.format(this.modifyDate);
	}

	@JsonGetter("createDate")
	public String getCreateDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		sdf.setLenient(false);
		return sdf.format(this.createDate);
	}

	public boolean isActive() {
		return isActive;
	}

	void activate() {
		this.isActive = true;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}