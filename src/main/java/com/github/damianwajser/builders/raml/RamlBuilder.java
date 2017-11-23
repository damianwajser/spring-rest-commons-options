package com.github.damianwajser.builders.raml;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.builders.OptionsBuilder;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.QueryString;

public class RamlBuilder implements OptionsBuilder<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RamlBuilder.class);

	private OptionsResult controller;

	public RamlBuilder(OptionsResult controller) {
		this.controller = controller;
	}

	public Map<String, Object> build() {
		return getEnpointsRaml(controller);
	}

	private Map<String, Object> getEnpointsRaml(OptionsResult controller) {
		Map<String, Object> root = new HashMap<>();
		Map<String, Map<String, Object>> endpoints = new HashMap<>();
		controller.getEnpoints().forEach(e -> {
			LOGGER.info("create raml for: " + e);
			String relativeUrl = e.getRelativeUrl().isEmpty() ? "/" : e.getRelativeUrl();
			if (!endpoints.containsKey(relativeUrl)) {
				endpoints.put(relativeUrl, getMethods(e));
			} else {
				endpoints.get(relativeUrl).putAll(getMethods(e));
			}
		});
		root.put(controller.getBaseUrl(), endpoints);
		return root;
	}

	private Map<String, Object> getMethods(Endpoint e) {
		Map<String, Object> methods = new HashMap<>();
		methods.put(e.getHttpMethod(), getMethodInfo(e));
		return methods;
	}

	private Object getMethodInfo(Endpoint e) {
		Map<String, Object> info = new HashMap<>();
		Optional<Map<String, Object>> queryParameters = getQueryParameters(e.getQueryString());
		if (queryParameters.isPresent()) {
			info.put("queryParameters", queryParameters.get());
		}
		return info;
	}

	private Optional<Map<String, Object>> getQueryParameters(QueryString queryString) {
		Map<String, Object> parameters = new HashMap<>();
		queryString.getParams().forEach(p -> parameters.put(p.getName(), getParameterInfo(p)));
		return parameters.isEmpty() ? Optional.empty() : Optional.of(parameters);
	}

	private Object getParameterInfo(Parameters p) {
		Map<String, Object> info = new HashMap<>();
		info.put("required", p.isRequired());
		info.put("type", p.getType());
		return info;
	}
}
