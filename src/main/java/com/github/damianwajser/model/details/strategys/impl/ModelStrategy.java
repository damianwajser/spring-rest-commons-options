package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class ModelStrategy extends DetailFieldStrategy {

	public ModelStrategy(Type type) {
		super(type);
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		// this.setClassOfType(Class.forName(this.getType().getTypeName()));
		Optional<Class<?>> clazz = ReflectionUtils.getClass(this.getType());
		while (clazz.isPresent()) {
			detailFields.addAll(createDetail(clazz.get(), isRequest));
			clazz = Optional.ofNullable(clazz.get().getSuperclass());
		}
		return detailFields;
	}

	private Collection<DetailField> createDetail(Class<?> clazz, boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		if (!Iterable.class.isAssignableFrom(clazz)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					Optional<DetailField> detail = createDetail(field, isRequest);
					if (detail.isPresent()) {
						detailFields.add(detail.get());
					}
				}
			}
		} else {
			detailFields = DetailFieldCreatedStrategyFactory
					.getCreationStrategy(this.getType(), Optional.empty())
					.createDetailField(isRequest);
		}
		return detailFields;
	}
}
