package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.JsonSchemmaUtils;

public class BodyResponse extends Body {

	public BodyResponse(Method m, Class<?> controllerClass) {
		super(m, controllerClass);
	}

	@Override
	protected Collection<DetailField> buildFields() {
		Class<?> returnType = this.getMethod().getReturnType();
		DetailFieldStrategy strategy = null;

		if (Iterable.class.isAssignableFrom(returnType) || ResponseEntity.class.isAssignableFrom(returnType)) {
			Type t = this.getMethod().getGenericReturnType();
			strategy = DetailFieldCreatedStrategyFactory.getCreationStrategy(t, this.getControllerClass(), true);
		} else {
			strategy = DetailFieldCreatedStrategyFactory.getCreationStrategy(Optional.of(returnType), false);
		}
		return strategy.createDetailField(false);
	}

	@Override
	protected JsonSchema fillJsonSchema() {
		return JsonSchemmaUtils.getSchemma(this.getMethod(), this.getControllerClass(), false).orElse(null);
	}
}
