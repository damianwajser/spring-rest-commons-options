package com.github.damianwajser.builders.raml;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.damianwajser.builders.raml.model.ResourceRaml;

public class Raml {

	public Raml(String title, String baseUri, String version) {
		super();
		this.title = title;
		this.baseUri = baseUri;
		this.version = version;
	}

	private String title;
	private String baseUri;
	private String version;
	
	@JsonUnwrapped
	private ResourceRaml resources;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ResourceRaml getResources() {
		return resources;
	}

	public void setResources(ResourceRaml resources) {
		this.resources = resources;
	}
}
