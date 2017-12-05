package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.response.DetailFieldResponseFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.JsonSchemmaUtils;

public class BodyResponse extends Body {
	public BodyResponse() {
	}

	public BodyResponse(Method m, Class<?> controllerClass) {
		super(m, controllerClass);
	}

	@Override
	protected List<DetailField> buildFields() {
		Type returnType = this.getMethod().getGenericReturnType();
		DetailFieldStrategy strategy = null;
		strategy = DetailFieldResponseFactory.getCreationStrategy(returnType, this.getParametrizedClass());
		return strategy.createDetailField(false);
	}

	@Override
	protected JsonSchema fillJsonSchema() {
		return JsonSchemmaUtils.getSchemma(this.getMethod(), this.getParametrizedClass(), false).orElse(null);
	}
}
