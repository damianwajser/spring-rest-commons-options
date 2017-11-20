package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.utils.JsonSchemmaUtils;
import com.github.damianwajser.utils.ReflectionUtils;

public class BodyRequest extends Body {

	public BodyRequest(Method m, Class<?> controllerClass) {
		super(m, controllerClass);
	}

	@Override
	protected Collection<DetailField> buildFields() {
		Collection<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getParameters(this.getMethod())
				.forEach(p -> detailFields.addAll(DetailFieldCreatedStrategyFactory
						.getCreationStrategy(p.getParameterizedType(), super.getControllerClass(), false).createDetailField(true)));
		return detailFields;
	}

	@Override
	protected JsonSchema fillJsonSchema() {
		return JsonSchemmaUtils.getSchemma(this.getMethod(), this.getControllerClass(), true).orElse(null);
	}

}
