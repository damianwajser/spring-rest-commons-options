package com.github.damianwajser.model.details.strategys;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.model.details.strategys.impl.ModelStrategy;
import com.github.damianwajser.model.details.strategys.impl.PrimitiveStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public final class DetailFieldCreatedStrategyFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetailFieldCreatedStrategyFactory.class);

	private DetailFieldCreatedStrategyFactory() {
	}

	// TODO: ver cuando se recive collections de objetos de negocio
	public static DetailFieldStrategy getCreationStrategy(Parameter p, Class<?> controller) {
		Type type = ReflectionUtils.getRealType(p.getParameterizedType(), controller);
		return getCreationStrategy(controller, type);

	}

	public static DetailFieldStrategy getCreationStrategy(Class<?> returnType, Class<?> controller) {
		Type type = ReflectionUtils.getRealType(returnType, controller);
		return getCreationStrategy(controller, type);

	}
	public static DetailFieldStrategy getCreationStrategy(Type returnType, Class<?> controller) {
		Type type = ReflectionUtils.getRealType(returnType, controller);
		return getCreationStrategy(controller, type);

	}

	private static DetailFieldStrategy getCreationStrategy(Class<?> controller, Type type) {
		LOGGER.debug("seleccionando strategyField: " + type.getTypeName());
		DetailFieldStrategy strategy = null;
		if (!ReflectionUtils.isJDKClass(type)) {
			strategy = new ModelStrategy(type, controller);
		} else {
			strategy = new PrimitiveStrategy(type, controller);
		}
		LOGGER.info("Se selecciono strategy " + strategy);
		return strategy;
	}

}