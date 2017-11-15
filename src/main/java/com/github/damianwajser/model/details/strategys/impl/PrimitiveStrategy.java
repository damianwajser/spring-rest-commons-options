package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.DetailFieldCollection;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class PrimitiveStrategy extends DetailFieldStrategy {

	public PrimitiveStrategy(Type type, Class<?> controller) {
		super(type, controller);
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getClass(this.getType()).ifPresent(clazz -> {
			if (Iterable.class.isAssignableFrom(clazz)) {
				DetailFieldCollection details = new DetailFieldCollection();				
				ReflectionUtils.getGenericType(this.getType())
						.ifPresent(type -> details.setCollection(DetailFieldCreatedStrategyFactory
								.getCreationStrategy(type, this.getController()).createDetailField(isRequest)));
				detailFields.add(details);
			}
		});
		return detailFields;
	}

}
