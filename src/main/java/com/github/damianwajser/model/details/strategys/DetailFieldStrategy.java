package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {
	private Type type;

	public abstract Collection<DetailField> createDetailField(boolean isRequest);

	public DetailFieldStrategy(Type type) {
		this.setType(type);
	}

	protected Optional<DetailField> createDetail(Field field, boolean isRequest) {
		Optional<DetailField> detailField = Optional.empty();
		if (!field.isAnnotationPresent(Autowired.class)) {
			if (isRequest) {
				detailField = Optional.ofNullable(
						new DetailFieldWithValidations(ValidatorFactory.getValidations(field).orElse(null)));
			} else {
				detailField = Optional.ofNullable(new DetailField());
			}
			detailField.ifPresent(d -> fillDetails(field, d));
		}
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
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
