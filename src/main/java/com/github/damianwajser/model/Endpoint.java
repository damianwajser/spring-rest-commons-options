package com.github.damianwajser.model;

import java.util.Collection;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.github.damianwajser.model.detailsFields.DetailField;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({ "endpoint", "httpMethod", "baseUrl", "relativeUrl" })
public class Endpoint implements Comparable<Endpoint> {

	@JsonUnwrapped
	private QueryString queryString;
	@JsonIgnore
	private RequestMethod[] methods;
	@JsonIgnore
	private String baseUrl;
	private String relativeUrl;
	private Collection<DetailField> bodyRequest;
	private Collection<DetailField> bodyResponse;

	public Endpoint(String url, String relativeUrl, RequestMethod[] httpRequestMethod, QueryString queryString) {
		this.setBaseUrl(url);
		this.setRelativeUrl(relativeUrl);
		this.methods = httpRequestMethod;
		this.queryString = queryString;
	}

	public String getHttpMethod() {
		return this.methods.length==0?"":this.methods[0].name();
	}

	public void setMethods(RequestMethod[] methods) {
		this.methods = methods;
	}

	public QueryString getQueryString() {
		return queryString;
	}

	public void setQueryString(QueryString queryString) {
		this.queryString = queryString;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	@Override
	@JsonGetter("endpoint")
	public String toString() {
		return this.getHttpMethod() + " - " + this.getBaseUrl() + this.getRelativeUrl()
				+ (queryString.toString().isEmpty() ? "" : "?" + queryString);
	}

	@Override
	public int compareTo(Endpoint o) {
		boolean isGet = this.getHttpMethod().equals(HttpMethod.GET.toString());
		return isGet ? 1 : this.getHttpMethod().compareTo(o.getHttpMethod());
	}

	public Collection<DetailField> getBodyRequest() {
		return bodyRequest;
	}

	public void setBodyRequest(Collection<DetailField> bodyRequest) {
		this.bodyRequest = bodyRequest;
	}

	public Collection<DetailField> getBodyResponse() {
		return bodyResponse;
	}

	public void setBodyResponse(Collection<DetailField> bodyResponse) {
		this.bodyResponse = bodyResponse;
	}
}
