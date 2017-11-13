package com.github.damianwajser.model.detailsFields.strategys.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.model.detailsFields.DetailField;
import com.github.damianwajser.model.detailsFields.strategys.DetailFieldStrategy;

public class PrimitiveStrategy implements DetailFieldStrategy{

	@Override
	public Collection<DetailField> createDetailField(boolean addAuditable) {
		Collection<DetailField> detailFields = new ArrayList<>();
		DetailField detail = new DetailField();
		detailFields.add(detail);
		return detailFields;
	}

}
