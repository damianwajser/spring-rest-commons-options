package com.github.damianwajser.model.validators;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.github.damianwajser.model.validators.impl.DefaultValidator;
import com.github.damianwajser.model.validators.impl.PatternValidator;
import com.github.damianwajser.model.validators.impl.RangeValidator;

public final class ValidatorFactory {
	private ValidatorFactory() {
	}

	private static List<String> validatorsPackage = Arrays.asList("javax.validation.constraints",
			"hibernate-validator");

	public static Optional<List<Validator>> getValidations(PropertyDescriptor fieldDescriptor, Optional<Field> field) {
		Optional<List<Validator>> validations = Optional.empty();
		validations = fillValidations(fieldDescriptor, validations);
		if (field.isPresent()) {
			List<Annotation> annotationField = Arrays.asList(field.get().getAnnotations());
			if (!annotationField.isEmpty()) {
				validations = completeValidations(validations, annotationField);
			}
		}
		return validations;
	}

	private static Optional<List<Validator>> completeValidations(Optional<List<Validator>> validations,
			List<Annotation> annotationField) {
		List<Validator> validatorsField = getValidators(annotationField);
		if (validations.isPresent()) {
			validations.get().addAll(validatorsField);
		} else {
			validations = Optional.ofNullable(validatorsField);
		}
		return validations.isPresent() && validations.get().isEmpty() ? Optional.empty() : validations;
	}

	private static Optional<List<Validator>> fillValidations(PropertyDescriptor fieldDescriptor,
			Optional<List<Validator>> validations) {
		List<Annotation> annotations = new ArrayList<>();
		Method setter = fieldDescriptor.getWriteMethod();
		Method getter = fieldDescriptor.getReadMethod();
		if (setter != null) {
			annotations.addAll(Arrays.asList(setter.getAnnotations()));
		}
		if (getter != null) {
			annotations.addAll(Arrays.asList(getter.getAnnotations()));
		}
		if (!annotations.isEmpty()) {
			validations = Optional.ofNullable(getValidators(annotations));
		}
		return validations;
	}

	private static List<Validator> getValidators(List<Annotation> annotations) {
		List<Validator> validations = new ArrayList<>();
		for (Annotation annotation : annotations) {
			getValidator(annotation).ifPresent(v -> validations.add(v));
		}
		return validations;
	}

	private static Optional<Validator> getValidator(Annotation annotation) {
		Validator validator = null;

		if (isValidable(annotation)) {
			if (annotation instanceof Range || annotation instanceof Length) {
				validator = new RangeValidator(annotation);
			} else if (annotation instanceof Pattern) {
				validator = new PatternValidator(annotation);
			} else {
				validator = new DefaultValidator(annotation);
			}
		}
		return Optional.ofNullable(validator);
	}

	private static boolean isValidable(Annotation annotation) {
		Package p = annotation.annotationType().getPackage();
		return p.getName() != null && validatorsPackage.contains(p.getImplementationTitle())
				|| validatorsPackage.contains(p.getName());
	}
}
