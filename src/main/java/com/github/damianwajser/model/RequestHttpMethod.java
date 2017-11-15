package com.github.damianwajser.model;

import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.RequestMethod;

import com.github.damianwajser.utils.ReflectionUtils;

public class RequestHttpMethod {

	private RequestMethod[] httpMethod;

	public RequestHttpMethod(Method m) {
		this.setHttpMethod(ReflectionUtils.getHttpRequestMethod(m).orElse(new RequestMethod[] {}));
	}

	public RequestMethod[] getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(RequestMethod[] httpMethod) {
		this.httpMethod = httpMethod;
	}
}
