package com.github.damianwajser.builders.raml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder("description")
public class ResourceRaml {

	private String description;
	private Map<String, List<Object>> resource;

	public ResourceRaml(String description) {
		this.description = description;
	}

	public ResourceRaml() {
		this(null);
	}

	public void add(String key, Object value) {
		if (resource == null)
			resource = new HashMap<>();
		if (!resource.containsKey(key)) {
			resource.put(key, new ArrayList<>());
		}
		resource.get(key).add(value);
	}

	@JsonAnyGetter
	public Map<String, List<Object>> getResource() {
		return this.resource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
