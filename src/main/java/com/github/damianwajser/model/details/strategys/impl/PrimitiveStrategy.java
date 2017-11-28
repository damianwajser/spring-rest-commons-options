package com.github.damianwajser.model.details.strategys.impl;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
