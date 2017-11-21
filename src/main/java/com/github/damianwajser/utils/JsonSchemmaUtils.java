package com.github.damianwajser.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public final class JsonSchemmaUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemmaUtils.class);

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
			LOGGER.error("Se produjo un error al crear el JsonSchemma para la clase {}", clazz.getSimpleName(), e);
		}
		return schema;
	}

	public static Optional<JsonSchema> getSchemma(Method m, Class<?> controller, boolean isRequest) {
		Optional<JsonSchema> schemma;
		if (isRequest) {
			schemma = getRequestSchemma(m, controller);
		} else {
			schemma = getResponseSchemma(m, controller);
		}
		return schemma;
	}

	private static Optional<JsonSchema> getResponseSchemma(Method m, Class<?> parametrizedClass) {
		Optional<JsonSchema> schemma = Optional.empty();
		// obtengo el tipo real de retorno por si es generic
		Optional<Type> returnType = ReflectionUtils.getRealType(m.getGenericReturnType(), parametrizedClass);
		if (returnType.isPresent()) {
			// el return type puede ser una colleccion para eso obtengo la clase real del
			// parametro a traves de reflection utils
			Optional<Class<?>> realClass = ReflectionUtils.getClass(returnType.get());
			if (realClass.isPresent()) {
				if (Iterable.class.isAssignableFrom(realClass.get())) {
					// si es una collection tengo que saber si es generic, para eso le pido la clase
					// al return type
					if (ParameterizedType.class.isAssignableFrom(returnType.get().getClass()))
						returnType = ReflectionUtils.getGenericType((ParameterizedType) returnType.get());
				}

				schemma = getSchemma(ReflectionUtils.getClass(returnType.orElse(null)).orElse(realClass.get()));
			} else {
				LOGGER.error("No existe una real class para: {}", returnType);
			}
		}
		return schemma;
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
				} else {
					schemma = getSchemma(t.getClass());
				}
			} else {
				schemma = getSchemma(ReflectionUtils.getClass(p.get(0).getParameterizedType()).get());
			}
		}
		return schemma;
	}
}
