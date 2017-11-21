package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public class PrimitiveStrategy extends DetailFieldStrategy {

	public PrimitiveStrategy(Type type) {
		super(type);
	}

	@Override
	public Collection<DetailField> createDetailField(boolean isRequest) {
		
		return Collections.emptyList();
	}

}
