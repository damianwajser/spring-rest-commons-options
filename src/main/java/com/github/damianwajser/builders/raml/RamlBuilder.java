package com.github.damianwajser.builders.raml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.QueryString;
import com.github.damianwajser.model.details.DetailField;

public class RamlBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(RamlBuilder.class);

	private CollectionResources resources;

	public RamlBuilder(CollectionResources resources) {
		this.resources = resources;
	}

	public Map<String, Object> build() {
		return getEnpointsRaml(resources);
	}

	private Map<String, Object> getEnpointsRaml(CollectionResources resources) {
		Map<String, Object> root = new HashMap<>();
		Map<String, Map<String, Object>> endpoints = new HashMap<>();
		resources.getResources().forEach((k, resource) -> {
			LOGGER.info("create raml for: " + k);
			resource.getEndpoints().forEach(e -> {
				String relativeUrl = e.getRelativeUrl().isEmpty() ? "/" : e.getRelativeUrl();
				if (!endpoints.containsKey(relativeUrl)) {
					endpoints.put(relativeUrl, getMethods(e, resources.getHttpCodes()));
				} else {
					endpoints.get(relativeUrl).putAll(getMethods(e, resources.getHttpCodes()));
				}
			});
			root.put(k, endpoints);
		});
		return root;

	}

	private Map<String, Object> getMethods(Endpoint e, Map<Integer, List<DetailField>> httpCodes) {
		Map<String, Object> methods = new HashMap<>();
		methods.put(e.getHttpMethod(), getMethodInfo(e, httpCodes));
		return methods;
	}

	private Object getMethodInfo(Endpoint e, Map<Integer, List<DetailField>> httpCodes) {
		Map<String, Object> info = new HashMap<>();
		Optional<Map<String, Object>> queryParameters = getQueryParameters(e.getQueryString());
		if (queryParameters.isPresent()) {
			info.put("description", "aca descripcion");
			info.put("queryParameters", queryParameters.get());
			info.put("responses", getHttpCodesInfo(httpCodes, e));
		}
		return info;
	}

	private Map<Integer, Object> getHttpCodesInfo(Map<Integer, List<DetailField>> httpCodes, Endpoint e) {
		Map<Integer, Object> http = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			http.put(HttpStatus.OK.value(), mapper.writeValueAsString(e.getBodyResponse().getJsonSchema()));
		} catch (JsonProcessingException e1) {
			LOGGER.error("error al parsear el JsonSchema: {}", e.getBodyResponse().getJsonSchema(), e1);
		}
		httpCodes.forEach((code, body) -> http.put(code, getBodyHttp(body)));
		return http;
	}

	private Map<String, Object> getBodyHttp(List<DetailField> body) {
		Map<String, Object> code = new HashMap<>();
		code.put("description", "algo");
		Map<String, Object> appJsonMap = new HashMap<>();
		Map<String, Object> bodyMap = new HashMap<>();
		appJsonMap.put("application/json", bodyMap);
		body.forEach(f -> {
			bodyMap.put("type", "jsonSchemma");
		});

		code.put("body", appJsonMap);
		return code;
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
