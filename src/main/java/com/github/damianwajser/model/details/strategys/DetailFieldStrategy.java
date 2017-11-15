package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {
	private Type type;
	private Class<?> controller;

	public abstract Collection<DetailField> createDetailField(boolean isRequest);

	public DetailFieldStrategy(Type type, Class<?> clazz) {
		this.setType(type);
		this.setController(clazz);
	}

	protected DetailField createDetail(Field field, boolean isRequest) {
		DetailField detailField = new DetailField();

		if (isRequest) {
			detailField = new DetailFieldWithValidations(ValidatorFactory.getValidations(field).orElse(null));
		}

		fillDetails(field, detailField);
		return detailField;
	}

	private void fillDetails(Field field, DetailField detailField) {
		detailField.setName(field.getName());
		detailField.setType(field.getType().getSimpleName());
		detailField.setAuditable(field.isAnnotationPresent(Auditable.class));
	}

	protected Type getType() {
		return type;
	}

	protected void setType(Type type) {
		this.type = type;
	}

	protected Class<?> getController() {
		return controller;
	}

	protected void setController(Class<?> clazz) {
		this.controller = clazz;
	}
}
