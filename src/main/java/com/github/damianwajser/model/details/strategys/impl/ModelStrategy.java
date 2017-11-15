package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public class ModelStrategy extends DetailFieldStrategy {

	public ModelStrategy(Type type, Class<?> controller) {
		super(type, controller);
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		Collection<DetailField> detailFields = new ArrayList<>();
		try {
			this.setController(Class.forName(this.getType().getTypeName()));
			while (this.getController() != null) {
				detailFields.addAll(createDetail(this.getController(), isRequest));
				this.setController(this.getController().getSuperclass());
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
