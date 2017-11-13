package com.github.damianwajser.model.detailsFields.strategys;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import com.github.damianwajser.model.detailsFields.strategys.impl.ModelStrategy;
import com.github.damianwajser.model.detailsFields.strategys.impl.PrimitiveStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public final class DetailFieldCreatedStrategyFactory {
	
	private DetailFieldCreatedStrategyFactory() {
	}

	// TODO: ver cuando se recive collections de objetos de negocio
	public static DetailFieldStrategy getCreationStrategy(Parameter p, Class<?> controller) {
		Type type = getRealType(p, controller);
		if (!ReflectionUtils.isJDKClass(type)) {
			return new ModelStrategy(type, controller);
		} else {
			return new PrimitiveStrategy();
		}

	}

	private static Type getRealType(Parameter p, Class<?> controller) {
		Type type = p.getParameterizedType();
		if (type.getClass().equals(TypeVariableImpl.class)) {
			type = ReflectionUtils.getGenericClass(controller);
		}
		return type;
	}
}