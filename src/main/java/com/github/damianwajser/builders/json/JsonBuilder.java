package com.github.damianwajser.builders.json;

import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.QueryString;
import com.github.damianwajser.utils.ReflectionUtils;

public class JsonBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonBuilder.class);

	private Object controller;
	private String url;
	@JsonUnwrapped
	private Collection<Method> methods;

	public JsonBuilder(Object obj) {
		LOGGER.info("create options to: " + obj.getClass().getSimpleName());
		this.controller = obj;
	}

	private void fillMethods() {
		this.methods = ReflectionUtils.getRequestMethods(controller);
	}

	private void fillBaseUrl() {
		String[] urls = ReflectionUtils.getUrls(controller).orElse(new String[] { "/" });
		if(urls.length>0){
			this.url=urls[0];
		}else{
			this.url="/";
		}
	}

	public OptionsResult build() {
		this.fillMethods();
		this.fillBaseUrl();
		return getResult();
	}

	private OptionsResult getResult() {
		OptionsResult response = new OptionsResult(this.url);

		this.methods.forEach(m -> {
			String relativeUrl = ReflectionUtils.getRelativeUrl(m);
			QueryString queryString = ReflectionUtils.getQueryString(m);
			RequestMethod[] httpMethod = ReflectionUtils.getHttpRequestMethod(m).orElse(new RequestMethod[] {});
			Endpoint endpoint = new Endpoint(this.url, relativeUrl, httpMethod, queryString);
			endpoint.setBodyRequest(ReflectionUtils.getRequestFieldDetail(m, controller.getClass()));
			endpoint.setBodyResponse(ReflectionUtils.getResponseFieldDetail(m, controller.getClass()));
			if (!endpoint.getHttpMethod().equals(HttpMethod.OPTIONS.toString())) {
				response.addEnpoint(endpoint);
			}
		});
		return response;
	}

}
