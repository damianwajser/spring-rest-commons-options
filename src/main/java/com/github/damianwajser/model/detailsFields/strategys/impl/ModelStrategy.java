package com.github.damianwajser.model.detailsFields.strategys.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.annotations.Auditable;
import com.github.damianwajser.model.detailsFields.DetailField;
import com.github.damianwajser.model.detailsFields.strategys.DetailFieldStrategy;

public class ModelStrategy implements DetailFieldStrategy{

	private Type type;
	private Class<?> clazz;
	
	
	public ModelStrategy(Type type, Class<?> clazz) {
		super();
		this.type = type;
		this.clazz = clazz;
	}
	@Override
	public Collection<DetailField> createDetailField(boolean addAuditable) {
		Collection<DetailField> detailFields = new ArrayList<>();
		try {
			clazz = Class.forName(type.getTypeName());
			while (clazz != null) {
				detailFields.addAll(createDetail(addAuditable, clazz));
				clazz = clazz.getSuperclass();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return detailFields;
	}
	private Collection<DetailField> createDetail(boolean addAuditable, Class<?> clazz) {
		Collection<DetailField> detailFields = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				if (addAuditable) {
					detailFields.add(createDetail(field));
				} else if (!field.isAnnotationPresent(Auditable.class)) {
					detailFields.add(createDetail(field));
				}
			}
		}
		return detailFields;
	}
}
