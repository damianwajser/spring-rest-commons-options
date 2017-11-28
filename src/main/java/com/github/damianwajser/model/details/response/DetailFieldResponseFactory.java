package com.github.damianwajser.model.details.response;

import java.lang.reflect.Type;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public final class DetailFieldResponseFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetailFieldResponseFactory.class);

	private DetailFieldResponseFactory() {
	}

	public static DetailFieldStrategy getCreationStrategy(Type t, Class<?> parametrizedClass) {
		LOGGER.info("Seleccionando estrategia para para el parametro del respomnse {}, con la clase parametrica {}", t,
				parametrizedClass);
		return DetailFieldCreatedStrategyFactory.getCreationStrategy(t, Optional.ofNullable(parametrizedClass));

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}