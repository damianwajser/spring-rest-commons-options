package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
	private static final Logger LOGGER = LoggerFactory.getLogger(Endpoint.class);
	private List<Endpoint> endpoints = new ArrayList<>();

	public List<Endpoint> getEndpoints() {
		Collections.sort(endpoints);
		for (Endpoint endpoint : endpoints) {
			LOGGER.error(endpoint.getEndpoint());
		}
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public void addEndpoint(Endpoint e) {
		endpoints.add(e);

	}

}
