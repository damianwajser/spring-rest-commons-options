package com.github.damianwajser.model.details.strategys;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.validators.Validator;
import com.github.damianwajser.model.validators.ValidatorFactory;

public abstract class DetailFieldStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(DetailFieldStrategy.class);

	private Type type;

	public abstract List<DetailField> createDetailField(boolean isRequest);

	public DetailFieldStrategy(Type type) {
		this.setType(type);
	}

	protected Optional<DetailField> createDetail(PropertyDescriptor descriptor, Optional<Field> f, boolean isRequest) {
		Optional<DetailField> detailField;
		LOGGER.debug("creando property: {}, field: {}, isRequest: {}", descriptor, f, isRequest);
		if (isRequest) {
			Optional<List<Validator>> validators = ValidatorFactory.getValidations(descriptor, f);
			if (validators.isPresent() && !validators.get().isEmpty()) {
				LOGGER.debug("se crea DetailFieldWithValidations, con las validaciones: {}", validators);
				detailField = Optional.ofNullable(new DetailFieldWithValidations(validators.get()));
			} else {
				detailField = Optional.ofNullable(new DetailField());
				LOGGER.debug("se crea Request DetailField : {}", descriptor.getName());
			}
		} else {
			detailField = Optional.ofNullable(new DetailField());
			LOGGER.debug("se crea Response DetailField : {}", descriptor.getName());
		}
		detailField.ifPresent(d -> fillDetails(descriptor, f, d, isRequest));
		return detailField;
	}

	private void fillDetails(PropertyDescriptor field, Optional<Field> optField, DetailField detailField,
			boolean isRequest) {
		detailField.setName(getName(field, optField, isRequest));
		detailField.setType(field.getPropertyType().getSimpleName());
		optField.ifPresent(f -> detailField.setAuditable(
				field.getWriteMethod() != null ? field.getWriteMethod().isAnnotationPresent(Auditable.class)
						: false || field.getReadMethod() != null
								? field.getReadMethod().isAnnotationPresent(Auditable.class)
								: false));
	}

	private String getName(PropertyDescriptor field, Optional<Field> optField, boolean isRequest) {
		String name = field.getName();
		if (optField.isPresent()) {
			JsonProperty property = optField.get().getAnnotation(JsonProperty.class);
			if (property != null) {
				name = property.value();
			} else if (isRequest) {
				name = getName(field.getReadMethod(), name);
			} else {
				name = getName(field.getWriteMethod(), name);
			}
		}
		return name;
	}

	private String getName(Method m, String defaultName) {
		String name = defaultName;
		if (m != null) {
			JsonProperty getter = m.getAnnotation(JsonProperty.class);
			if (getter != null) {
				name = getter.value();
			}
		}
		return name;
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
