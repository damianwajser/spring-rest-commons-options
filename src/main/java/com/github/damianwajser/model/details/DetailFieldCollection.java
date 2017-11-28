package com.github.damianwajser.model.details;

import java.util.Collections;
import java.util.List;

public class DetailFieldCollection extends DetailField{

	private List<DetailField> collection;

	public DetailFieldCollection() {
		super.setType("collection");
	}
	public List<DetailField> getCollection() {
		Collections.sort(collection, (f,f1)->f.getName().compareTo(f1.getName()));
		return collection;
	}

	public void setCollection(List<DetailField> collection) {
		this.collection = collection;
	}
}
