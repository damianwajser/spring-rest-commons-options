package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;
import com.github.damianwajser.utils.ReflectionUtils;

public class CollectionStrategy extends DetailFieldStrategy {

	public CollectionStrategy(Type type) {
		super(type);
	}

	@Override
	public List<DetailField> createDetailField(boolean isRequest) {
		List<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getClass(this.getType()).ifPresent(clazz -> {
			DetailFieldCollection details = new DetailFieldCollection();
			Optional<Type> type = ReflectionUtils.getRealType(clazz);

			if (ParameterizedType.class.isAssignableFrom(clazz))
				type = ReflectionUtils.getGenericType((ParameterizedType) this.getType());

			type.ifPresent(t -> details.setCollection(DetailFieldCreatedStrategyFactory
					.getCreationStrategy(t, Optional.empty()).createDetailField(isRequest)));
			detailFields.add(details);
		});
		return detailFields;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
