package com.github.damianwajser.model.details;

import java.util.Collection;

import com.github.damianwajser.model.validators.impl.Validator;

public class DetailFieldWithValidations extends DetailField {

	private Collection<Validator> validation;

	public DetailFieldWithValidations(Collection<Validator> validation) {
		super();
		this.setValidation(validation);
	}

	public Collection<Validator> getValidation() {
		return validation;
	}

	public void setValidation(Collection<Validator> validation) {
		this.validation = validation;
	}

}
