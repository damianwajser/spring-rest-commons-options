package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.damianwajser.model.body.Body;
import com.github.damianwajser.model.body.BodyRequest;
import com.github.damianwajser.model.body.BodyResponse;
import com.github.damianwajser.model.header.Header;
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
	private BodyRequest bodyRequest;
	private Body bodyResponse;
	private List<Header> headers;

	public Endpoint(String url, String relativeUrl, Method m, Object controller) {
		this.setBaseUrl(url);
		this.setRelativeUrl(relativeUrl);
		this.setPathVariable(new PathVariable(m, this.getRelativeUrl()));
		this.methods = new RequestHttpMethod(m);
		this.queryString = new QueryString(m);
		this.setBodyRequest(new BodyRequest(m, controller.getClass()));
		this.setBodyResponse(new BodyResponse(m, controller.getClass()));
		headers = new ArrayList<>();
		ReflectionUtils.getHeaders(m).forEach(h -> {
			RequestHeader rh = h.getDeclaredAnnotationsByType(RequestHeader.class)[0];
			headers.add(new Header(rh.value(), h.getType().getSimpleName()));
		});
	}

	public String getHttpMethod() {
		return this.methods.getHttpMethod().length == 0 ? "" : this.methods.getHttpMethod()[0].name();
	}

	public QueryString getQueryString() {
		return queryString;
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

	@JsonGetter("endpoint")
	public String getEndpoint() {
		return this.getHttpMethod() + " - " + this.getBaseUrl() + this.getRelativeUrl()
				+ (queryString.toString().isEmpty() ? "" : "?" + queryString);
	}

	public BodyRequest getBodyRequest() {
		return bodyRequest;
	}

	public void setBodyRequest(BodyRequest bodyRequest) {
		this.bodyRequest = bodyRequest;
	}

	public Body getBodyResponse() {
		return bodyResponse;
	}

	public void setBodyResponse(Body bodyResponse) {
		this.bodyResponse = bodyResponse;
	}

	public PathVariable getPathVariable() {
		return pathVariable;
	}

	public void setPathVariable(PathVariable pathVariable) {
		this.pathVariable = pathVariable;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, true);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, true);
	}

	@Override
	public int compareTo(Endpoint o) {
		return this.getEndpoint().compareTo(o.getEndpoint());
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}
}
