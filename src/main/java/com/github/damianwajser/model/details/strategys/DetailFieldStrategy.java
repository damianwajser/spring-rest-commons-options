package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Field;
import java.util.Collection;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {

	 protected DetailField createDetail(Field field) {
		DetailField detailField = new DetailField();
		detailField.setName(field.getName());
		detailField.setType(field.getType().getSimpleName());
		detailField.setValidation(ValidatorFactory.getValidations(field).orElse(null));
		return detailField;
	}

	public abstract Collection<DetailField> createDetailField(boolean addAuditable);
}
