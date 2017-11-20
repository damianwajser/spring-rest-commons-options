package com.github.damianwajser.model.details.request;

import java.lang.reflect.Parameter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public final class DetailFieldRequestFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetailFieldRequestFactory.class);

	private DetailFieldRequestFactory() {
	}

	public static DetailFieldStrategy getCreationStrategy(Parameter p, Class<?> parametrizedClass) {
		LOGGER.info("Seleccionando estrategia para para el parametro del request {} , con la clase parametrica {}", p,
				parametrizedClass);
		return DetailFieldCreatedStrategyFactory.getCreationStrategy(p.getParameterizedType(),
				Optional.ofNullable(parametrizedClass));

	}

}