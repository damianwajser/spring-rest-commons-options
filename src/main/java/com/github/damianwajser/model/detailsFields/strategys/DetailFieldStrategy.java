package com.github.damianwajser.model.detailsFields.strategys;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.detailsFields.DetailField;

public interface DetailFieldStrategy {

	 default DetailField createDetail(Field field) {
		DetailField detailField = new DetailField();
		detailField.setName(field.getName());
		detailField.setType(field.getType().getSimpleName());
		detailField.setValidation(getValidations(field));
		return detailField;
	}

	default Collection<String> getValidations(Field field) {
		Collection<String> validations = new ArrayList<>();
		Annotation[] annotations = field.getDeclaredAnnotations();
		if (annotations.length > 0) {
			for (Annotation annotation : annotations) {
				Package p = annotation.annotationType().getPackage();
				if (p.getImplementationTitle() != null && p.getImplementationTitle().equals("hibernate-validator")) {
					validations.add(annotation.annotationType().getSimpleName());
				}
			}
		}
		return validations;
	}

	Collection<DetailField> createDetailField(boolean addAuditable);
}
