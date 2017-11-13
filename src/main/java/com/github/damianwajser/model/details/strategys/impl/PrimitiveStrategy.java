package com.github.damianwajser.model.details.strategys.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;

public class PrimitiveStrategy extends DetailFieldStrategy{

	@Override
	public Collection<DetailField> createDetailField(boolean addAuditable) {
		Collection<DetailField> detailFields = new ArrayList<>();
		DetailField detail = new DetailField();
		detailFields.add(detail);
		return detailFields;
	}

}
