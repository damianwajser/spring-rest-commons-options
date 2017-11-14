package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public class ModelStrategy extends DetailFieldStrategy {

	private Type type;
	private Class<?> clazz;

	public ModelStrategy(Type type, Class<?> clazz) {
		super();
		this.type = type;
		this.clazz = clazz;
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		try {
			clazz = Class.forName(type.getTypeName());
			while (clazz != null) {
				detailFields.addAll(createDetail(clazz, isRequest));
				clazz = clazz.getSuperclass();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return detailFields;
	}

	private Collection<DetailField> createDetail(Class<?> clazz, boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				detailFields.add(createDetail(field, isRequest));
			}
		}
		return detailFields;
	}
}
