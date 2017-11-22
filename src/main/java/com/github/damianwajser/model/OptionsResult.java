package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonPropertyOrder("baseUrl")
public class OptionsResult {
	@JsonUnwrapped
	private List<Endpoint> enpoints = new ArrayList<>();
	private String baseUrl;

	public OptionsResult(String baseUrl) {
		super();
		this.baseUrl = baseUrl;
	}

	public List<Endpoint> getEnpoints() {
		Collections.sort(enpoints);
		return enpoints;
	}

	public void setEnpoints(List<Endpoint> enpoints) {
		this.enpoints = enpoints;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void addEnpoint(Endpoint endpoint) {
		this.enpoints.add(endpoint);

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
