package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.utils.JsonSchemmaUtils;
import com.github.damianwajser.utils.ReflectionUtils;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({ "endpoint", "httpMethod", "baseUrl", "relativeUrl" })
public class Endpoint implements Comparable<Endpoint> {

	private QueryString queryString;

	private PathVariable pathVariable;
	@JsonIgnore
	private RequestHttpMethod methods;
	@JsonIgnore
	private String baseUrl;
	private String relativeUrl;
	private Collection<DetailField> bodyRequest;
	private Collection<DetailField> bodyResponse;
	private JsonSchema bodyRequestSchema = null;
	private JsonSchema bodyResponseSchema = null;

	public Endpoint(String url, String relativeUrl, Method m, Object controller) {
		this.setBaseUrl(url);
		this.setRelativeUrl(relativeUrl);
		this.methods = new RequestHttpMethod(m);
		this.queryString = new QueryString(m);
		this.setPathVariable(new PathVariable(m, this.getRelativeUrl()));
		this.setBodyRequest(ReflectionUtils.getRequestFieldDetail(m, controller.getClass()));
		this.setBodyResponse(ReflectionUtils.getResponseFieldDetail(m, controller.getClass()));
		this.bodyRequestSchema = JsonSchemmaUtils.getSchemma(m, controller.getClass(), true).orElse(null);
		this.bodyResponseSchema = JsonSchemmaUtils.getSchemma(m, controller.getClass(), false).orElse(null);
	}

	public String getHttpMethod() {
		return this.methods.getHttpMethod().length == 0 ? "" : this.methods.getHttpMethod()[0].name();
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

	public PathVariable getPathVariable() {
		return pathVariable;
	}

	public void setPathVariable(PathVariable pathVariable) {
		this.pathVariable = pathVariable;
	}

	public JsonSchema getBodyRequestSchema() {
		return bodyRequestSchema;
	}

	public void setBodyRequestSchema(JsonSchema bodyRequestSchema) {
		this.bodyRequestSchema = bodyRequestSchema;
	}

	public JsonSchema getBodyResponseSchema() {
		return bodyResponseSchema;
	}

	public void setBodyResponseSchema(JsonSchema bodyResponseSchema) {
		this.bodyResponseSchema = bodyResponseSchema;
	}
}
