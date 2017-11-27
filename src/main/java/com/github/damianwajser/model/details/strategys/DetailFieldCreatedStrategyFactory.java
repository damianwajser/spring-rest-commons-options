package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
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
		DetailFieldStrategy strategy = null;
		if (type != null) {
			LOGGER.debug("seleccionando strategyField: {}", type.getTypeName());
			if (ParameterizedType.class.isAssignableFrom(type.getClass())
					|| TypeVariable.class.isAssignableFrom(type.getClass())) {
				LOGGER.debug("Es una clase parametrizada");
				strategy = getGenericParameterStrategy(type, parametrizedClass);
			} else {
				if (!ReflectionUtils.isJDKClass(type)) {
					LOGGER.debug("Es una clase de modelo {}", type);
					strategy = new ModelStrategy(ReflectionUtils.getRealType(type, parametrizedClass).orElse(null));
				} else {
					LOGGER.debug("Es una clase primitiva {}", type);
					strategy = new PrimitiveStrategy(type);
				}
			}
		}
		LOGGER.info("Se selecciono strategy {}", strategy);
		return strategy;
	}

	private static DetailFieldStrategy getGenericParameterStrategy(Type type, Optional<Class<?>> parametrizableClass) {
		DetailFieldStrategy strategy = null;
		// es un tipo generico y tengo que obtener la info de la clase
		Optional<Type> genericType = ReflectionUtils.getRealType(type, parametrizableClass.get());
		LOGGER.debug("Clase generica : {}, para la clase:{}, del tipo: {}", parametrizableClass.orElse(null),
				genericType, ((ParameterizedType) type).getActualTypeArguments()[0].getClass());
		// si la clase contenedora del parametro es collection
		if (Iterable.class.isAssignableFrom(ReflectionUtils.getClass(type).get())) {
			if (parametrizableClass.isPresent()) {
				strategy = new CollectionStrategy(genericType.orElse(null));
			} else {
				strategy = new ModelStrategy(genericType.orElse(null));
			}
		}
		return strategy;
	}
}