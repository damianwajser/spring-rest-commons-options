package com.github.damianwajser.model.validators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

	public static Optional<List<Validator>> getValidations(Field field) {
		Optional<List<Validator>> validations = Optional.empty();
		Annotation[] annotations = field.getDeclaredAnnotations();
		if (annotations.length > 0) {
			validations = Optional.ofNullable(getValidators(annotations));
		}
		return validations;
	}

	private static List<Validator> getValidators(Annotation[] annotations) {
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
			} else if (annotation instanceof Pattern){
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
