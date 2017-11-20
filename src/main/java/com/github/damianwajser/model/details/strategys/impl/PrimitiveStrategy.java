package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class PrimitiveStrategy extends DetailFieldStrategy {

	private boolean isCollection;

	public PrimitiveStrategy(Type type) {
		this(type, false);
	}

	public PrimitiveStrategy(Type type, boolean isCollection) {
		super(type);
		this.isCollection = isCollection;
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getClass(this.getType()).ifPresent(clazz -> {
			if (Iterable.class.isAssignableFrom(clazz) || isCollection) {
				DetailFieldCollection details = new DetailFieldCollection();
				Optional<Type> type = ReflectionUtils.getRealType(clazz);

				if (ParameterizedType.class.isAssignableFrom(clazz))
					type = ReflectionUtils.getGenericType((ParameterizedType) this.getType());

				type.ifPresent(t -> details.setCollection(DetailFieldCreatedStrategyFactory
						.getCreationStrategy(Optional.of(t), false).createDetailField(isRequest)));
				detailFields.add(details);
			}
		});
		return detailFields;
	}

}
