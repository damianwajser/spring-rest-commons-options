package com.github.damianwajser.model.validators.impl;

import java.lang.annotation.Annotation;

import com.github.damianwajser.model.validators.Validator;

public class DefaultValidator extends Validator{

	public DefaultValidator(Annotation annotation) {
		super(annotation);
	}

}
