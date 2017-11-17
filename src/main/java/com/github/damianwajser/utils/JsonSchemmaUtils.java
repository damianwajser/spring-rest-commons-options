package com.github.damianwajser.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
		}else{
			schemma = getResponseSchemma(m);
		}
		return schemma;
	}

	private static Optional<JsonSchema> getResponseSchemma(Method m) {
		Class<?> returnType = m.getReturnType();

		if (Iterable.class.isAssignableFrom(returnType)) {
			returnType = ReflectionUtils.getClass(m.getGenericReturnType()).orElse(returnType);
		}
		return getSchemma(returnType);
	}

	private static Optional<JsonSchema> getRequestSchemma(Method m, Class<?> controller) {
		List<Parameter> p = ReflectionUtils.getParameters(m);
		Optional<JsonSchema> schemma = Optional.empty();
		if (!p.isEmpty()) {
			Optional<Class<?>> c = ReflectionUtils.getClass(ReflectionUtils.getRealType(p.get(0).getParameterizedType(), controller));
			if (c.isPresent()) {
				schemma = getSchemma(c.get());
			}
		}
		return schemma;
	}
}
