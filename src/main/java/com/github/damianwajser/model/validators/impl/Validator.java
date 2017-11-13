package com.github.damianwajser.model.validators.impl;

import java.lang.annotation.Annotation;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Validator {
	private Annotation annotation;

	public Validator(Annotation annotation) {
		super();
		this.annotation = annotation;
	}

	public String getName() {
		return this.annotation.annotationType().getSimpleName();
	}

	protected Annotation getAnnotation() {
		return this.annotation;
	}

	@JsonIgnore
	public Optional<String> getMessage() {
		return Optional.ofNullable((String) AnnotationUtils.getValue(this.annotation, "message"));
	}

	@JsonGetter("message")
	public String getMessageStr() {
		return this.getMessage().orElse("");
	}
}
