package com.github.damianwajser.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.QueryString;
import com.github.damianwajser.model.RequestParams;
import com.github.damianwajser.model.detailsFields.DetailField;
import com.github.damianwajser.model.detailsFields.strategys.DetailFieldCreatedStrategyFactory;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public final class ReflectionUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

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

	public static Type getGenericClass(Class clazz) {
		return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public static Collection<DetailField> getFieldDetail(Method m, Class<?> controller, boolean addAuditable) {
		Collection<DetailField> fields = new ArrayList<>();
		Arrays.asList(m.getParameters()).stream().filter(p -> {
			boolean ok = p.getAnnotation(PathVariable.class) == null;
			ok = ok && p.getAnnotation(RequestParam.class) == null;
			return ok && p.getAnnotation(RequestHeader.class) == null;
		}).forEach(p1 -> fields.addAll(
				DetailFieldCreatedStrategyFactory.getCreationStrategy(p1, controller).createDetailField(addAuditable)));

		return fields;
	}

	public static boolean isJDKClass(Type t) {
		return t.getTypeName().startsWith("java");
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

	public static QueryString getQueryString(Method m) {
		QueryString q = new QueryString();
		Arrays.asList(m.getParameters()).forEach(parameter -> {
			RequestParam a = parameter.getAnnotation(RequestParam.class);
			if (a != null) {
				String typeStr = parameter.getType().getSimpleName();
				Type type = parameter.getParameterizedType();
				if (type instanceof ParameterizedTypeImpl) {
					typeStr = ((Class<?>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[0]).getSimpleName();
				}
				q.add(new RequestParams(a.required(), a.value(), typeStr));
			}
		});
		return q;
	}
}
