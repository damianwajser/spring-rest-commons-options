package com.github.damianwajser.model.validators.impl;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;

import com.github.damianwajser.model.validators.Validator;

public class PatternValidator extends Validator {
	
	private String regularExpression = ""; 

	public PatternValidator(Annotation annotation) {
		super(annotation);
		this.setRegularExpression(((String)AnnotationUtils.getValue(annotation,"regexp")));
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}


}
