package com.github.damianwajser.model.validators;

import java.lang.annotation.Annotation;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.core.annotation.AnnotationUtils;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
