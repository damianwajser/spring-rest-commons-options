package com.github.damianwajser.builders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.DetailField;
import com.github.damianwajser.model.DetailResponse;

public class OptionBuilder {

	private Object object;

	public OptionBuilder(Object obj) {
		this.object = obj;
	}

	@SuppressWarnings({ "unchecked" })
	private DetailResponse getAnnotationValue(Class<?> clazz, Method m,
			@SuppressWarnings("rawtypes") Class... anotations) {
		DetailResponse detail = new DetailResponse();
		Object value = null;
		int i = 0;
		while (i < anotations.length && value == null) {
			Annotation annotation = m.getAnnotation(anotations[i]);
			if (annotation != null) {
				value = AnnotationUtils.getValue(annotation);
				if (AnnotationUtils.getValue(annotation, "method") == null) {
					annotation = annotation.annotationType().getDeclaredAnnotation(RequestMapping.class);
				}
				detail.setMethod((RequestMethod[]) AnnotationUtils.getValue(annotation, "method"));
			}
			i++;
		}
		detail.setUrl(((String[]) AnnotationUtils.getValue(clazz.getAnnotation(RequestMapping.class)))[0]);
		detail.setResource(value != null ? (String[]) value : null);
		return detail;
	}

	private Optional<DetailResponse> getRequestMappingAnnotation(Class<?> clazz, Method m) {

		return Optional.ofNullable(getAnnotationValue(clazz, m, RequestMapping.class, PutMapping.class,
				DeleteMapping.class, PostMapping.class, GetMapping.class, PatchMapping.class));
	}

	private Type getGenericClass(Class clazz) {
		return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private Collection<String> getValidations(Field field) {
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

	public Collection<DetailResponse> build() {
		Collection<DetailResponse> response = new ArrayList<>();
		for (Method m : this.object.getClass().getMethods()) {
			Collection<DetailField> fieldsRequest = getFieldDetail(object.getClass(), false);
			Collection<DetailField> fieldsResponse = getFieldDetail(object.getClass(), true);
			if (!m.getDeclaringClass().equals(Object.class)) {
				this.getRequestMappingAnnotation(object.getClass(), m).ifPresent(detail -> {
					if (!m.isAnnotationPresent(GetMapping.class)) {
						detail.setBodyRequest(fieldsRequest);
					}
					detail.setBodyResponse(fieldsResponse);
					response.add(detail);
				});
			}
		}
		return response;
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
}
