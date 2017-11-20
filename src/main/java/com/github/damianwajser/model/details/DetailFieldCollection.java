package com.github.damianwajser.model.details;

import java.util.Collection;

public class DetailFieldCollection extends DetailField{

	private Collection<DetailField> collection;

	public DetailFieldCollection() {
		super.setType("collection");
	}
	public Collection<DetailField> getCollection() {
		return collection;
	}

	public void setCollection(Collection<DetailField> collection) {
		this.collection = collection;
	}
}
