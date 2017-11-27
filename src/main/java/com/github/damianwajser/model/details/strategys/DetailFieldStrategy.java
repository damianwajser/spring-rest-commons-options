package com.github.damianwajser.model.details.strategys;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {
	private Type type;

	public abstract List<DetailField> createDetailField(boolean isRequest);

	public DetailFieldStrategy(Type type) {
		this.setType(type);
	}

	protected Optional<DetailField> createDetail(PropertyDescriptor descriptor, Optional<Field> f, boolean isRequest) {
		Optional<DetailField> detailField;
		if (isRequest) {
			detailField = Optional.ofNullable(
					new DetailFieldWithValidations(ValidatorFactory.getValidations(descriptor, f).orElse(null)));
		} else {
			detailField = Optional.ofNullable(new DetailField());
		}
		detailField.ifPresent(d -> fillDetails(descriptor, f, d));
		return detailField;
	}

	private void fillDetails(PropertyDescriptor field, Optional<Field> optField, DetailField detailField) {
		detailField.setName(field.getName());
		detailField.setType(field.getPropertyType().getSimpleName());
		optField.ifPresent(f -> detailField.setAuditable(
				field.getWriteMethod() != null ? field.getWriteMethod().isAnnotationPresent(Auditable.class)
						: false || field.getReadMethod() != null
								? field.getReadMethod().isAnnotationPresent(Auditable.class)
								: false));
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
