package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.request.DetailFieldRequestFactory;
import com.github.damianwajser.model.details.response.DetailFieldResponseFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.JsonSchemmaUtils;
import com.github.damianwajser.utils.ReflectionUtils;

public class BodyRequest extends Body {

	public BodyRequest(Method m, Class<?> controllerClass) {
		super(m, controllerClass);
	}

	@Override
	protected List<DetailField> buildFields() {
		List<DetailField> detailFields = new ArrayList<>();
		ReflectionUtils.getParameters(this.getMethod())
				.forEach(p -> detailFields.addAll(DetailFieldRequestFactory
						.getCreationStrategy(p, super.getParametrizedClass()).createDetailField(true)));
		return detailFields;
	}

	@Override
	protected JsonSchema fillJsonSchema() {
		return JsonSchemmaUtils.getSchemma(this.getMethod(), this.getParametrizedClass(), true).orElse(null);
	}

}
