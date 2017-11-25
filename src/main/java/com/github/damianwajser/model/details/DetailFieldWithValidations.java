package com.github.damianwajser.model.details;

import java.util.List;

import com.github.damianwajser.model.validators.Validator;

public class DetailFieldWithValidations extends DetailField {

	private List<Validator> validation;

	public DetailFieldWithValidations(List<Validator> validation) {
		super();
		this.setValidation(validation);
	}

	public List<Validator> getValidation() {
		return validation;
	}

	public void setValidation(List<Validator> validation) {
		this.validation = validation;
	}

}
