package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"fullUrl","method", "url", "resource"})
public class DetailResponse {
	private String[] resource = new String[] {};
	private RequestMethod[] method;
	private String url;
	private Collection<DetailField> bodyRequest;
	private Collection<DetailField> bodyResponse;

	public RequestMethod[] getMethod() {
		return method;
	}

	public void setMethod(RequestMethod[] requestMethods) {
		this.method = requestMethods;
	}

	public String[] getResource() {
		if (this.resource == null) {
			return new String[] { "/" };
		} else {
			return resource;
		}
	}

	public void setResource(String[] resource) {
		this.resource = resource;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String string) {
		this.url = string;
	}

	public Collection<String> getFullUrl() {
		Collection<String> urls = new ArrayList<>();
		if (this.getResource() != null) {
			for (String string : this.getResource()) {
				urls.add(this.getUrl() + string);
			}
		}
		return urls;
	}

	public Collection<DetailField> getBodyRequest() {
		return bodyRequest;
	}

	public void setBodyRequest(Collection<DetailField> fields2) {
		this.bodyRequest = fields2;
	}

	public Collection<DetailField> getBodyResponse() {
		return bodyResponse;
	}

	public void setBodyResponse(Collection<DetailField> bodyResponse) {
		this.bodyResponse = bodyResponse;
	}
}
