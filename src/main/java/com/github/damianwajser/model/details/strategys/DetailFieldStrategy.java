package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Field;
import java.util.Collection;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {

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

	public abstract Collection<DetailField> createDetailField(boolean isRequest);
}
