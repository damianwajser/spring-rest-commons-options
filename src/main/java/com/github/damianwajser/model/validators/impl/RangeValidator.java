package com.github.damianwajser.model.validators.impl;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;

public class RangeValidator extends Validator {
	
	private Long min = 0l;
	private Long max = Long.MAX_VALUE;

	public RangeValidator(Annotation annotation) {
		super(annotation);
		this.setMin(((Number)AnnotationUtils.getValue(annotation,"min")).longValue());
		this.setMax(((Number)AnnotationUtils.getValue(annotation,"max")).longValue());
	}

	public Long getMin() {
		return min;
	}

	protected void setMin(Long min) {
		this.min = min;
	}

	public Long getMax() {
		return max;
	}

	protected void setMax(Long max) {
		this.max = max;
	}

}
