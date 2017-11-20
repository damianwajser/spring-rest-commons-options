package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Parameter;
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

	public static DetailFieldStrategy getCreationStrategy(Type t, Class<?> parametrizedClass, boolean isCollection) {
		Optional<Type> type = Optional.empty();
		// si el controller es parametrico <Paramenter>
		type = ReflectionUtils.getRealType(t, parametrizedClass);
		return getCreationStrategy(type, isCollection);

	}

	public static DetailFieldStrategy getCreationStrategy(Class<?> returnType, Class<?> parametrizedClass,
			boolean isCollection) {
		Optional<Type> type = ReflectionUtils.getRealType(returnType, parametrizedClass);
		return getCreationStrategy(type, isCollection);

	}

	public static DetailFieldStrategy getCreationStrategy(Optional<Type> type, boolean isCollection) {
		DetailFieldStrategy strategy = null;
		if (type.isPresent()) {
			LOGGER.debug("seleccionando strategyField: " + type.get().getTypeName());
			if (!ReflectionUtils.isJDKClass(type.get()) && !isCollection) {
				strategy = new ModelStrategy(type.get());
			} else {
				strategy = new PrimitiveStrategy(type.get(), isCollection);
			}
		} else {
			strategy = new PrimitiveStrategy(type.orElse(null));
		}
		LOGGER.info("Se selecciono strategy " + strategy);
		return strategy;
	}

}