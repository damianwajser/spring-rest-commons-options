package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(Endpoint.class);
	private QueryString queryString;

	private PathVariable pathVariable;
	@JsonIgnore
	private RequestHttpMethod methods;
	@JsonIgnore
	private String baseUrl;
	private String relativeUrl;
	private BodyRequest bodyRequest;
	private BodyResponse bodyResponse;
	private List<Header> headers;

	public Endpoint() {
	}

	public Endpoint(String relativeUrl, Method m, Object controller) {
		this.baseUrl = fillBaseUrl(controller);
		this.setRelativeUrl(relativeUrl);
		this.setPathVariable(new PathVariable(m, this.getRelativeUrl()));
		this.methods = new RequestHttpMethod(m);
		this.queryString = new QueryString(m);
		this.setBodyRequest(new BodyRequest(m, controller.getClass()));
		this.setBodyResponse(new BodyResponse(m, controller.getClass()));
		headers = new ArrayList<>();
		ReflectionUtils.getHeaders(m).forEach(h -> {
			LOGGER.debug("obteniendo el header: {}", h);
			RequestHeader rh = h.getDeclaredAnnotationsByType(RequestHeader.class)[0];
			headers.add(new Header(rh.value(), h.getType().getSimpleName()));
		});
	}

	private String fillBaseUrl(Object controller) {
		String[] urls = getUrls(controller).orElse(new String[] { "/" });
		String url;
		if (urls.length > 0) {
			url = urls[0];
		} else {
			url = "/";
		}
		return url;
	}

	private Optional<String[]> getUrls(Object controller) {
		return Optional.ofNullable(
				(String[]) AnnotationUtils.getValue(controller.getClass().getAnnotation(RequestMapping.class)));
	}

	public String getHttpMethod() {
		return this.methods == null || this.methods.getHttpMethod().length == 0 ? ""
				: this.methods.getHttpMethod()[0].name();
	}

	public QueryString getQueryString() {
		return queryString;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	@JsonGetter("endpoint")
	public String getEndpoint() {
		return this.getHttpMethod() + " - " + this.getUrl()
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

	public void setBodyResponse(BodyResponse bodyResponse) {
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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

	public String getUrl() {
		return this.getBaseUrl() + this.getRelativeUrl();
	}
}
