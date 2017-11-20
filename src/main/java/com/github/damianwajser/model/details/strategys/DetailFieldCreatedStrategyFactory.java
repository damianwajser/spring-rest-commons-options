package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.model.details.strategys.impl.ModelStrategy;
import com.github.damianwajser.model.details.strategys.impl.PrimitiveStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public final class DetailFieldCreatedStrategyFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetailFieldCreatedStrategyFactory.class);

	private DetailFieldCreatedStrategyFactory() {
	}

	public static DetailFieldStrategy getCreationStrategy(Type type, Optional<Class<?>> parametrizedClass) {
		LOGGER.debug("seleccionando strategyField: " + type.getTypeName());
		DetailFieldStrategy strategy = null;
		if (type != null) {
			if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
				strategy = getGenericParameterStrategy(type, strategy);
			} else {
				if (!ReflectionUtils.isJDKClass(type)) {
					strategy = new ModelStrategy(ReflectionUtils.getRealType(type, parametrizedClass).get());
				} else {
					strategy = new PrimitiveStrategy(type);
				}
			}
		}
		LOGGER.info("Se selecciono strategy " + strategy);
		return strategy;
	}

	private static DetailFieldStrategy getGenericParameterStrategy(Type type, DetailFieldStrategy strategy) {
		// es un tipo generico y tengo que obtener la info de la clase
		Optional<Class<?>> genericClazz = ReflectionUtils.getClass(((ParameterizedType) type).getRawType());
		// si la clase contenedora del parametro es collection
		Type genericType = ReflectionUtils.getGenericType((ParameterizedType) type).orElseThrow(RuntimeException::new);
		if (Iterable.class.isAssignableFrom(genericClazz.get())) {
			strategy = new CollectionStrategy(genericType);
		} else {
			strategy = new ModelStrategy(genericType);
		}
		return strategy;
	}

}