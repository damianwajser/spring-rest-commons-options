package com.github.damianwajser.model.validators.impl;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;

public class RangeValidator extends Validator {
	
	private Integer min = 0;
	private Integer max = Integer.MAX_VALUE;

	public RangeValidator(Annotation annotation) {
		super(annotation);
		this.setMin((Integer)AnnotationUtils.getValue(annotation,"min"));
		this.setMax((Integer)AnnotationUtils.getValue(annotation,"max"));
	}

	public Integer getMin() {
		return min;
	}

	protected void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	protected void setMax(Integer max) {
		this.max = max;
	}

}
