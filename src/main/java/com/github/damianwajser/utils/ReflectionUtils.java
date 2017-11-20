package com.github.damianwajser.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.model.Parameters;

public final class ReflectionUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);
	private static final List<String> PRIMITIVES = Arrays.asList("boolean", "byte", "char", "short", "int", "long",
			"float", "double", "void");

	private ReflectionUtils() {
	};

	private static Collection<Class<? extends Annotation>> requestAnnotation = Arrays.asList(RequestMapping.class,
			PutMapping.class, DeleteMapping.class, PostMapping.class, GetMapping.class, PatchMapping.class);

	public static boolean containsRequestAnnotation(Method m) {
		LOGGER.debug("check conteins annotation " + m.getName());
		return filterRequestMappingAnnontations(m).findAny().isPresent();

	}

	public static Stream<Annotation> filterRequestMappingAnnontations(Method m) {
		return Arrays.asList(m.getAnnotations()).stream().filter(a -> requestAnnotation.contains(a.annotationType()));
	}

	public static Optional<Type> getGenericType(Class<?> clazz) {
		Optional<Type> t = Optional.empty();
		if (clazz != null && clazz.getGenericSuperclass() != null
				&& clazz.getGenericSuperclass() instanceof ParameterizedType)
			t = Optional.ofNullable(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
		return t;
	}

	public static Optional<Type> getGenericType(ParameterizedType t) {
		return Optional.ofNullable(((ParameterizedType) t).getActualTypeArguments()[0]);
	}

	public static Optional<Class<?>> getGenericClass(Class<?> clazz) {
		Type t = getGenericType(clazz).orElse(clazz);
		return getClass(t);
	}

	public static List<Parameter> getParameters(Method m) {
		return Arrays.asList(m.getParameters()).stream().filter(p -> {
			boolean ok = p.getAnnotation(PathVariable.class) == null;
			ok = ok && p.getAnnotation(RequestParam.class) == null;
			return ok && p.getAnnotation(RequestHeader.class) == null;
		}).collect(Collectors.toList());
	}

	public static Optional<Type> getRealType(Type type, Optional<Class<?>> parametrizedClass){
		Optional<Type> optType = Optional.empty();
		if(parametrizedClass.isPresent()) {
			optType = getRealType(type, parametrizedClass.get());
		} else {
			optType = getRealType(type);
		}
		return optType;
		
	}
	public static Optional<Type> getRealType(Type type, Class<?> parametrizedClass){
		Optional<Type> optType = Optional.empty();
		if(isParametrizedClass(parametrizedClass) && !Void.TYPE.equals(type)) {
			optType = getRealType(((ParameterizedType)parametrizedClass.getGenericSuperclass()).getActualTypeArguments()[0]);
		}else {
			optType = getRealType(type);
		}
		return optType;
		
	}

	public static boolean isParametrizedClass(Class<?> parametrizedClass) {
		return ParameterizedType.class.isAssignableFrom(parametrizedClass.getGenericSuperclass().getClass());
	}
	
	public static Optional<Type> getRealType(Type type) {
		Optional<Type> t = Optional.ofNullable(type);
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			t = ReflectionUtils.getGenericType(type.getClass());
		} else {
			// type = ReflectionUtils.getGenericType(type).get();
		}
		return t;
	}

	public static boolean isJDKClass(Type t) {
		return t.getTypeName().startsWith("java") || PRIMITIVES.contains(t.getTypeName());
	}

	public static Collection<Parameters> getQueryString(Method m) {
		Collection<Parameters> parameters = new ArrayList<>();
		Arrays.asList(m.getParameters()).forEach(parameter -> {
			RequestParam a = parameter.getAnnotation(RequestParam.class);
			collectParameters(parameters, parameter, a);
		});
		return parameters;
	}

	private static void collectParameters(Collection<Parameters> parameters, Parameter parameter, Annotation a) {
		if (a != null) {
			String typeStr = parameter.getType().getSimpleName();
			Type type = parameter.getParameterizedType();
			if (type instanceof ParameterizedType) {
				typeStr = ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).getSimpleName();
			}
			parameters.add(new Parameters((boolean) AnnotationUtils.getValue(a, "required"),
					(String) AnnotationUtils.getValue(a), typeStr));
		}
	}

	public static List<Parameters> getPathVariable(Method m) {
		List<Parameters> parameters = new ArrayList<>();
		Arrays.asList(m.getParameters()).forEach(parameter -> {
			PathVariable a = parameter.getAnnotation(PathVariable.class);
			collectParameters(parameters, parameter, a);
		});
		return parameters;
	}

	public static Optional<Class<?>> getClass(Type type) {
		Optional<Class<?>> clazz = Optional.empty();
		if (type != null) {
			if (type instanceof Class) {
				clazz = Optional.of((Class<?>) type);
			} else if (type instanceof ParameterizedType) {
				clazz = getClass(((ParameterizedType) type).getRawType());
			}else if (TypeVariable.class.isAssignableFrom(type.getClass())) {
				clazz = getClass(((TypeVariable<?>)type).getClass());
			}
		}
		return clazz;
	}

	public static Optional<Class<?>> getRealClass(Class<?> type) {
		return getClass(getRealType(type).orElse(null));
	}
}
