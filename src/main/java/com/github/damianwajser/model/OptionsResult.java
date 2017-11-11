package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionsResult {
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
}
