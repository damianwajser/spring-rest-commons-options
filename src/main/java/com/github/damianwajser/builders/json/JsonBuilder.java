package com.github.damianwajser.builders.json;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.damianwajser.builders.OptionsBuilder;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.utils.ReflectionUtils;

public class JsonBuilder implements OptionsBuilder{

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
		if (urls.length > 0) {
			this.url = urls[0];
		} else {
			this.url = "/";
		}
	}

	public Optional<OptionsResult> build() {
		this.fillMethods();
		this.fillBaseUrl();
		Optional<OptionsResult> result = getResult();
		result.ifPresent(o -> fixEndpoints(o));
		return result;
	}

	private void fixEndpoints(OptionsResult result) {
		if (result.getBaseUrl().equals("/")) {
			Map<String, Integer> count = new HashMap<>();
			result.getEnpoints().forEach(e -> {
				String[] relatives = e.getRelativeUrl().split("/");

				for (int i = 0; i < relatives.length; i++) {
					if (!StringUtils.isEmpty(relatives[i])) {
						Integer num = count.get(relatives[i]) == null ? 1 : count.get(relatives[i]) + 1;
						count.put(relatives[i], num);
					}
				}
			});
			LOGGER.info("Contador de arbol: " + count);
			String realBaseUrl = "/"+Collections
					.max(count.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
			LOGGER.info("real url for: " + realBaseUrl);
			result.setBaseUrl(realBaseUrl);
			result.getEnpoints().forEach(e -> e.setBaseUrl(realBaseUrl));
		}
	}

	private Optional<OptionsResult> getResult() {
		Optional<OptionsResult> response = Optional.empty();
		getRealController();
		String packageName = this.controller.getClass().getPackage().getName();
		if (!packageName.startsWith("org.springframework.boot.autoconfigure.web")) {
			OptionsResult result = new OptionsResult(this.url);

			this.methods.forEach(m -> {
				String relativeUrl = ReflectionUtils.getRelativeUrl(m);
				Endpoint endpoint = new Endpoint(this.url, relativeUrl, m, controller);
				if (!endpoint.getHttpMethod().equals(HttpMethod.OPTIONS.toString())) {
					result.addEnpoint(endpoint);
				}
			});
			response = Optional.of(result);
		}
		return response;
	}

	private void getRealController() {
		if (AopUtils.isAopProxy(this.controller)) {
			try {
				LOGGER.debug(this.controller + " spring proxy, get real object");
				this.controller = ((Advised) this.controller).getTargetSource().getTarget();
				LOGGER.debug("Real Object: " + this.controller);
			} catch (Exception e) {
				LOGGER.error("Problemas al obtener el controller: " + this.controller, e);
			}
		}
	}

}
