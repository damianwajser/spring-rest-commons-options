package com.github.damianwajser.builders.raml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder("description")
public class ResourceMethodRaml extends ResourceRaml {

	private Map<String, Collection<QueryParameterRaml>> queryParameters;

	public Map<String, Collection<QueryParameterRaml>> getQueryParameters() {
		return queryParameters;
	}

	public void addQueryParameters(String name, QueryParameterRaml parameter) {
		if (queryParameters == null)
			queryParameters = new HashMap<>();
		if (!queryParameters.containsKey(name)) {
			queryParameters.put(name, new ArrayList<>());
		}
		queryParameters.get(name).add(parameter);
	}
}
