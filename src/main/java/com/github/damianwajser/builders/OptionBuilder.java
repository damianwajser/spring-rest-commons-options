package com.github.damianwajser.builders;

import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.QueryString;
import com.github.damianwajser.utils.ReflectionUtils;

public class OptionBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptionBuilder.class);
	
	private Object controller;
	private String url;
	private Collection<Method> methods;

	public OptionBuilder(Object obj) {
		LOGGER.info("create options to: " + obj.getClass().getSimpleName());
		this.controller = obj;
	}

	private void fillMethods() {
		this.methods = ReflectionUtils.getRequestMethods(controller);
	}

	private void fillBaseUrl() {
		this.url = ReflectionUtils.getUrls(controller).orElse(new String[]{"/"})[0];
	}

	public OptionsResult build() {
		this.fillMethods();
		this.fillBaseUrl();
		OptionsResult response = new OptionsResult(this.url);

		this.methods.forEach(m -> {
			String relativeUrl = ReflectionUtils.getRelativeUrl(m);
			QueryString queryString = ReflectionUtils.getQueryString(m);
			RequestMethod[] httpMethod = ReflectionUtils.getHttpRequestMethod(m).orElse(new RequestMethod[]{});
			Endpoint endpoint = new Endpoint(this.url, relativeUrl,httpMethod ,
					queryString);
			if (!endpoint.getHttpMethod().equals(HttpMethod.OPTIONS.toString())) {
				System.out.println(endpoint.toString());
				response.addEnpoint(endpoint);
			}
		});

		return response;
	}

}
