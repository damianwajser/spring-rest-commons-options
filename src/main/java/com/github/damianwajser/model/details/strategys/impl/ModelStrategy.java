package com.github.damianwajser.model.details.strategys.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class ModelStrategy extends DetailFieldStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelStrategy.class);

	public ModelStrategy(Type type) {
		super(type);
	}

	@Override
	public List<DetailField> createDetailField(boolean isRequest) {
		List<DetailField> detailFields = new ArrayList<>();
		Optional<Class<?>> clazz = ReflectionUtils.getClass(this.getType());
		clazz.ifPresent(c -> detailFields.addAll(createDetail(c, isRequest)));
		return detailFields;
	}

	private List<DetailField> createDetail(Class<?> c, boolean isRequest) {
		List<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getGenericClass(c).ifPresent(clazz -> {
			try {
				for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz, Object.class)
						.getPropertyDescriptors()) {
					if (!propertyDescriptor.getReadMethod().getDeclaringClass().equals(Object.class)) {
						Optional<Field> field = getField(clazz, propertyDescriptor);
						if (checkIfAddField(field, propertyDescriptor, isRequest)) {
							Optional<DetailField> detail = super.createDetail(propertyDescriptor, field, isRequest);
							detail.ifPresent(d -> detailFields.add(d));
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error al inspeccionar la clase {}", clazz, e);
			}
		});
		return detailFields;
	}

	private Optional<Field> getField(Class<?> clazz, PropertyDescriptor p) {
		Optional<Field> res = Optional.empty();
		if (clazz != null) {
			try {
				res = Optional.ofNullable(clazz.getDeclaredField(p.getName()));
			} catch (NoSuchFieldException | SecurityException e) {
				String typeName = p.getName();
				if (p.getPropertyType().equals(Boolean.TYPE) && p.getReadMethod().getName().startsWith("is")) {
					typeName = "is" + StringUtils.capitalize(typeName);
					try {
						res = Optional.ofNullable(clazz.getDeclaredField(typeName));
					} catch (NoSuchFieldException | SecurityException e1) {
						LOGGER.error("error al obtener el campo: {}, de la clase {}", p.getName(), clazz);
						res = getField(clazz.getSuperclass(), p);
					}
				} else {
					LOGGER.error("error al obtener el campo: {}, de la clase {}", p.getName(), clazz);
					res = getField(clazz.getSuperclass(), p);
				}
			}
		}
		return res;
	}

	private boolean checkIfAddField(Optional<Field> field, PropertyDescriptor propertyDescriptor, boolean isRequest) {
		boolean res = true;
		if (field.isPresent()) {
			res = !field.get().isAnnotationPresent(JsonIgnore.class);
			if (isRequest) {
				res = res && !field.get().isAnnotationPresent(Auditable.class);
				res = res && propertyDescriptor.getReadMethod() != null;
				res = res && !propertyDescriptor.getReadMethod().isAnnotationPresent(Auditable.class);
				Method setter = propertyDescriptor.getWriteMethod();
				if (setter != null)
					res = res && !setter.isAnnotationPresent(Auditable.class);
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
