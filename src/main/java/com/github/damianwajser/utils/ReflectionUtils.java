package com.github.damianwajser.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldCreatedStrategyFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;


public final class ReflectionUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);
	private static final List<String> PRIMITIVES = Arrays.asList("boolean", "byte", "char", "short", "int", "long",
			"float", "double", "void");

	private ReflectionUtils() {
	};

	private static Collection<Class<? extends Annotation>> requestAnnotation = Arrays.asList(RequestMapping.class,
			PutMapping.class, DeleteMapping.class, PostMapping.class, GetMapping.class, PatchMapping.class);

	private static boolean containsRequestAnnotation(Method m) {
		LOGGER.debug("check conteins annotation " + m.getName());
		return filterRequestMappingAnnontations(m).findAny().isPresent();

	}

	private static Stream<Annotation> filterRequestMappingAnnontations(Method m) {
		return Arrays.asList(m.getAnnotations()).stream().filter(a -> requestAnnotation.contains(a.annotationType()));
	}

	private static Annotation getRequestMqpping(Annotation annotation) {
		if (AnnotationUtils.getValue(annotation, "method") == null) {
			annotation = annotation.annotationType().getDeclaredAnnotation(RequestMapping.class);
		}
		return annotation;
	}

	public static String getRelativeUrl(Method m) {
		StringBuilder relativeUrl = new StringBuilder();
		// me quedo con la annotation (alguna de la lista)
		Annotation annotation = filterRequestMappingAnnontations(m).findFirst().get();
		Optional<Object> value = Optional.ofNullable(AnnotationUtils.getValue(annotation));
		value.ifPresent(v -> relativeUrl.append(Arrays.asList((String[]) v).stream().findFirst().orElse("")));
		return relativeUrl.toString();
	}

	public static Optional<String[]> getUrls(Object controller) {
		return Optional.ofNullable(
				(String[]) AnnotationUtils.getValue(controller.getClass().getAnnotation(RequestMapping.class)));
	}

	public static Optional<RequestMethod[]> getHttpRequestMethod(Method m) {
		Annotation a = filterRequestMappingAnnontations(m).findFirst().get();
		return Optional.ofNullable((RequestMethod[]) AnnotationUtils.getValue(getRequestMqpping(a), "method"));
	}

	public static Optional<Type> getGenericType(Class<?> clazz) {
		Optional<Type> t = Optional.empty();
		if (clazz != null && clazz.getGenericSuperclass() != null && clazz.getGenericSuperclass() instanceof ParameterizedType)
			t = Optional.ofNullable(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
		return t;
	}

	public static Optional<Type> getGenericType(Type t) {
		return Optional.ofNullable(((ParameterizedType) t).getActualTypeArguments()[0]);
	}

	public static Optional<Class<?>> getGenericClass(Class<?> clazz) {
		Type t = getGenericType(clazz).orElse(clazz);
		return getClass(t);
	}

	public static Collection<DetailField> getRequestFieldDetail(Method m, Class<?> controller) {
		Collection<DetailField> fields = new ArrayList<>();
		getParameters(m).forEach(p -> fields
				.addAll(DetailFieldCreatedStrategyFactory.getCreationStrategy(p, controller).createDetailField(true)));

		return fields;
	}

	public static List<Parameter> getParameters(Method m) {
		return Arrays.asList(m.getParameters()).stream().filter(p -> {
			boolean ok = p.getAnnotation(PathVariable.class) == null;
			ok = ok && p.getAnnotation(RequestParam.class) == null;
			return ok && p.getAnnotation(RequestHeader.class) == null;
		}).collect(Collectors.toList());
	}

	public static Type getRealType(Type type, Class<?> controller) {
		if (TypeVariable.class.isAssignableFrom(type.getClass())) {
			type = ReflectionUtils.getGenericType(controller).get();
		}
		return type;
	}

	public static Collection<DetailField> getResponseFieldDetail(Method m, Class<?> controller) {
		Class<?> returnType = m.getReturnType();
		DetailFieldStrategy strategy = null;

		if (Iterable.class.isAssignableFrom(returnType)) {
			Type t = m.getGenericReturnType();
			strategy = DetailFieldCreatedStrategyFactory.getCreationStrategy(t, controller);
		} else {
			strategy = DetailFieldCreatedStrategyFactory.getCreationStrategy(returnType, controller);
		}
		return strategy.createDetailField(false);
	}

	public static boolean isJDKClass(Type t) {
		return t.getTypeName().startsWith("java") || PRIMITIVES.contains(t.getTypeName());
	}

	/**
	 * Gets the request methods.
	 *
	 * @param object
	 *            to extract methods (Controller or RestController)
	 * @return the methos only represent the request controller
	 */
	public static Collection<Method> getRequestMethods(Object object) {
		return Arrays.asList(object.getClass().getMethods()).stream().filter(m -> containsRequestAnnotation(m))
				.collect(Collectors.toList());
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
			} else {
				clazz = getClass(((ParameterizedType) type).getRawType());
			}
		}
		return clazz;
	}

	public static Optional<Class<?>> getRealClass(Class<?> type, Class<?> controller) {
		return getClass(getRealType(type, controller));
	}
}
