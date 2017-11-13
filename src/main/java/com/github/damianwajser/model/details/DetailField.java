package com.github.damianwajser.model.details;

import java.util.Collection;

import com.github.damianwajser.model.validators.Validator;

public class DetailField {

	private String name;
	private String type;
	private Collection<Validator> validation;

	public Collection<Validator> getValidation() {
		return validation;
	}

	public void setValidation(Collection<Validator> validation) {
		this.validation = validation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
