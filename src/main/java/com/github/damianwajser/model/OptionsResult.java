package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.damianwajser.model.details.DetailField;

@JsonPropertyOrder("baseUrl")
public class OptionsResult {
	@JsonUnwrapped
	private List<Endpoint> enpoints = new ArrayList<>();
	private String baseUrl;
	@JsonProperty("httpCodes")
	private Map<Integer, List<DetailField>> httpCodes = new HashMap<>();
	
	public Map<Integer, List<DetailField>> getHttpCodes() {
		return httpCodes;
	}

	public void setHttpCodes(Map<Integer, List<DetailField>> httpCodes) {
		this.httpCodes = httpCodes;
	}

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
