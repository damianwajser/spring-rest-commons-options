package com.github.damianwajser.model.validators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.github.damianwajser.model.validators.impl.DefaultValidator;

public final class ValidatorFactory {
	private ValidatorFactory() {
	}

	private static List<String> validatorsPackage = Arrays.asList("javax.validation.constraints", "hibernate-validator");

	public static Optional<Collection<Validator>> getValidations(Field field) {
		Optional<Collection<Validator>> validations = Optional.empty();
		Annotation[] annotations = field.getDeclaredAnnotations();
		if (annotations.length > 0) {
			validations = Optional.ofNullable(getValidators(annotations));
		}
		return validations;
	}

	private static Collection<Validator> getValidators(Annotation[] annotations) {
		Collection<Validator> validations = new ArrayList<>();
		for (Annotation annotation : annotations) {
			getValidator(annotation).ifPresent(v -> validations.add(v));
		}
		return validations;
	}

	private static Optional<Validator> getValidator(Annotation annotation) {
		Optional<Validator> v = Optional.empty();
		Package p = annotation.annotationType().getPackage();
		if (p.getName() != null && validatorsPackage.contains(p.getImplementationTitle()) || validatorsPackage.contains(p.getName())) {
			v = Optional.of(new DefaultValidator(annotation));
		}
		return v;
	}
}
