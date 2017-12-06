package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
	private static final Logger LOGGER = LoggerFactory.getLogger(Endpoint.class);
	private SortedSet<Endpoint> endpoints = new TreeSet<>();

	public List<Endpoint> getEndpoints() {
		LOGGER.debug("Solicitando los endpoints: {}", endpoints);
		return new ArrayList<>(endpoints);
	}

	public void addEndpoint(Endpoint e) {
		endpoints.add(e);

	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
