package com.github.damianwajser.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public final class JsonSchemmaUtils {
	private JsonSchemmaUtils() {
	}

	private static Optional<JsonSchema> getSchemma(Class<?> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		Optional<JsonSchema> schema = Optional.empty();
		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		Optional<Class<?>> realClazz = ReflectionUtils.getGenericClass(clazz);
		boolean iterable = Iterable.class.isAssignableFrom(clazz) && realClazz.isPresent();
		if (iterable) {
			clazz = realClazz.get();
		}

		try {
			mapper.acceptJsonFormatVisitor(clazz, visitor);
			JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
			schema = Optional.ofNullable(schemaGen.generateSchema(clazz));
			if (iterable) {
				// TODO: decirle que es una collection
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		}
		return schema;
	}

	public static Optional<JsonSchema> getSchemma(Method m, Class<?> controller, boolean isRequest) {
		Optional<JsonSchema> schemma = Optional.empty();
		if (isRequest) {
			schemma = getRequestSchemma(m, controller);
		} else {
			schemma = getResponseSchemma(m, controller);
		}
		return schemma;
	}

	private static Optional<JsonSchema> getResponseSchemma(Method m, Class<?> parametrizedClass) {
		Optional<Type> returnType = Optional.of(m.getReturnType());

		if (Iterable.class.isAssignableFrom(ReflectionUtils.getClass(returnType.get()).get())) {
			returnType = ReflectionUtils.getRealType(null, parametrizedClass);
		}
		return getSchemma(ReflectionUtils.getClass(returnType.get()).get());
	}

	private static Optional<JsonSchema> getRequestSchemma(Method m, Class<?> controller) {
		List<Parameter> p = ReflectionUtils.getParameters(m);
		Optional<JsonSchema> schemma = Optional.empty();
		if (!p.isEmpty()) {
			Optional<Type> t = ReflectionUtils.getRealType(p.get(0).getParameterizedType(), controller);
			if (t.isPresent()) {
				Optional<Class<?>> c = ReflectionUtils.getClass(t.get());
				if (c.isPresent()) {
					schemma = getSchemma(c.get());
				}
			}
		}
		return schemma;
	}
}
