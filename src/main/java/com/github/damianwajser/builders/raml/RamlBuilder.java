package com.github.damianwajser.builders.raml;

public class RamlBuilder {

//	private static final Logger LOGGER = LoggerFactory.getLogger(RamlBuilder.class);
//
//	private OptionsResult controller;
//
//	public RamlBuilder(CollectionResources resources) {
//		this.controller = controller;
//	}
//
//	public Map<String, Object> build() {
//		return getEnpointsRaml(controller);
//	}
//
//	private Map<String, Object> getEnpointsRaml(OptionsResult controller) {
//		Map<String, Object> root = new HashMap<>();
//		Map<String, Map<String, Object>> endpoints = new HashMap<>();
//		controller.getEnpoints().forEach(e -> {
//			LOGGER.info("create raml for: " + e);
//			String relativeUrl = e.getRelativeUrl().isEmpty() ? "/" : e.getRelativeUrl();
//			if (!endpoints.containsKey(relativeUrl)) {
//				endpoints.put(relativeUrl, getMethods(e));
//			} else {
//				endpoints.get(relativeUrl).putAll(getMethods(e));
//			}
//		});
//		root.put(controller.getBaseUrl(), endpoints);
//		return root;
//	}
//
//	private Map<String, Object> getMethods(Endpoint e) {
//		Map<String, Object> methods = new HashMap<>();
//		methods.put(e.getHttpMethod(), getMethodInfo(e));
//		return methods;
//	}
//
//	private Object getMethodInfo(Endpoint e) {
//		Map<String, Object> info = new HashMap<>();
//		Optional<Map<String, Object>> queryParameters = getQueryParameters(e.getQueryString());
//		if (queryParameters.isPresent()) {
//			info.put("queryParameters", queryParameters.get());
//		}
//		return info;
//	}
//
//	private Optional<Map<String, Object>> getQueryParameters(QueryString queryString) {
//		Map<String, Object> parameters = new HashMap<>();
//		queryString.getParams().forEach(p -> parameters.put(p.getName(), getParameterInfo(p)));
//		return parameters.isEmpty() ? Optional.empty() : Optional.of(parameters);
//	}
//
//	private Object getParameterInfo(Parameters p) {
//		Map<String, Object> info = new HashMap<>();
//		info.put("required", p.isRequired());
//		info.put("type", p.getType());
//		return info;
//	}
}
