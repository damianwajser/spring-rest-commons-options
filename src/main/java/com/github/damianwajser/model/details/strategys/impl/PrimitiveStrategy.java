package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public class PrimitiveStrategy extends DetailFieldStrategy {

	public PrimitiveStrategy(Type type) {
		super(type);
	}

	@Override
	public List<DetailField> createDetailField(boolean isRequest) {

		return Collections.emptyList();
	}

}
