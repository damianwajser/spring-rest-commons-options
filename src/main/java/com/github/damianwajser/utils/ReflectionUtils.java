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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.DetailField;
import com.github.damianwajser.model.QueryString;
import com.github.damianwajser.model.RequestParams;

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

	private Type getGenericClass(Class clazz) {
		return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private Collection<DetailField> getFieldDetail(Class<?> sourceClass, boolean addAuditble) {
		Collection<DetailField> fields = new ArrayList<>();
		Class<?> clazz = (Class<?>) this.getGenericClass(sourceClass);
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					if (addAuditble) {
						fields.add(createDetail(field));
					} else if (!field.isAnnotationPresent(Auditable.class)) {
						fields.add(createDetail(field));
					}
				}
			}

			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	private DetailField createDetail(Field field) {
		DetailField detailField = new DetailField();
		detailField.setName(field.getName());
		detailField.setType(field.getType().getSimpleName());
		detailField.setValidation(this.getValidations(field));
		return detailField;
	}

	public static Collection<String> getValidations(Field field) {
		Collection<String> validations = new ArrayList<>();
		Annotation annotations[] = field.getDeclaredAnnotations();
		if (annotations.length > 0) {
			for (Annotation annotation : annotations) {
				Package p = annotation.annotationType().getPackage();
				if (p.getImplementationTitle() != null && p.getImplementationTitle().equals("hibernate-validator")) {
					validations.add(field.getDeclaredAnnotations()[0].annotationType().getSimpleName());
				}
			}
		}
		return validations;
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
			if (a != null)
				q.add(new RequestParams(a.required(), a.value(), parameter.getType().getSimpleName()));
		});
		return q;
	}

	public Collection<DetailField> getRequestParamentersDetail(Method m) {
		Collection<Parameter> parameters = Arrays.asList(m.getParameters());
		// parameters.forEach(p->get);
		return null;
	}
}
