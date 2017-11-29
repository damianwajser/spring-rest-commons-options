package com.github.damianwajser.builders.json;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.damianwajser.builders.OptionsBuilder;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.response.DetailFieldResponseFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class JsonBuilder implements OptionsBuilder<Optional<OptionsResult>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonBuilder.class);

	@JsonIgnore
	private static Collection<Object> exceptionHandlers;

	@JsonIgnore
	private Object controller;

	private String url;

	@JsonUnwrapped
	private Collection<Method> methods;

	public JsonBuilder(Object obj) {
		this(obj, null);
	}

	public JsonBuilder(Object obj, Collection<Object> handlers) {
		LOGGER.info("create options to: {}", obj.getClass().getSimpleName());
		this.controller = obj;
		if (exceptionHandlers == null)
			exceptionHandlers = handlers;
	}

	private void fillMethods() {
		this.methods = Arrays.asList(this.controller.getClass().getMethods()).stream()
				.filter(m -> ReflectionUtils.containsRequestAnnotation(m)).collect(Collectors.toList());
	}

	private void fillBaseUrl() {
		String[] urls = getUrls(controller).orElse(new String[] { "/" });
		if (urls.length > 0) {
			this.url = urls[0];
		} else {
			this.url = "/";
		}
	}

	private Optional<String[]> getUrls(Object controller) {
		return Optional.ofNullable(
				(String[]) AnnotationUtils.getValue(controller.getClass().getAnnotation(RequestMapping.class)));
	}

	public Optional<OptionsResult> build() {
		getRealController();
		this.fillMethods();
		this.fillBaseUrl();
		Optional<OptionsResult> result = getResult();
		result.ifPresent(o -> fixEndpoints(o));
		result.ifPresent(r -> buildExceptions(r));
		return result;
	}

	private void buildExceptions(OptionsResult r) {
		if (exceptionHandlers != null) {
			exceptionHandlers.forEach(handler -> {
				Arrays.asList(getRealObject(handler).getClass().getDeclaredMethods()).stream()
						.filter(m -> m.isAnnotationPresent(ExceptionHandler.class)
								&& m.isAnnotationPresent(ResponseStatus.class))
						.forEach(m -> {
							ResponseStatus status = m.getAnnotation(ResponseStatus.class);
							Type returnType = m.getGenericReturnType();
							DetailFieldStrategy strategy = null;
							strategy = DetailFieldResponseFactory.getCreationStrategy(returnType,
									m.getDeclaringClass());
							r.getHttpCodes().put(status.value().value(), strategy.createDetailField(false));
						});
			});
		}
	}

	private void fixEndpoints(OptionsResult result) {
		if (result.getBaseUrl().equals("/") && !result.getEnpoints().isEmpty()) {
			Map<String, Integer> count = new HashMap<>();
			result.getEnpoints().forEach(e -> countRelativeUrls(count, e));
			LOGGER.info("Contador de arbol: {}", count);
			if (!count.isEmpty()) {
				Entry<String, Integer> realBaseUrl = Collections.max(count.entrySet(),
						(entry1, entry2) -> entry1.getValue() - entry2.getValue());
				if (realBaseUrl.getValue() > 1) {
					LOGGER.info("real url for: {}", realBaseUrl.getKey());
					result.setBaseUrl(realBaseUrl.getKey());
					result.getEnpoints().forEach(e -> e.setBaseUrl(realBaseUrl.getKey()));
				}
			}
		}
	}

	private void countRelativeUrls(Map<String, Integer> count, Endpoint e) {
		LOGGER.info("fixeando relative url: {}", e.getRelativeUrl());
		String[] relatives = e.getRelativeUrl().split("/");
		String url = "";
		for (int i = 0; i < relatives.length; i++) {
			if (!StringUtils.isEmpty(relatives[i])) {
				url = url + "/" + relatives[i];
				Integer num = count.get(url) == null ? 1 : count.get(url) + 1;
				count.put(url, num);
			}
		}
	}

	private Optional<OptionsResult> getResult() {
		Optional<OptionsResult> response = Optional.empty();
		String packageName = this.controller.getClass().getPackage().getName();
		if (!packageName.startsWith("org.springframework.boot.autoconfigure.web")) {
			OptionsResult result = new OptionsResult(this.url);

			this.methods.forEach(m -> {
				String relativeUrl = getRelativeUrl(m);
				Endpoint endpoint = new Endpoint(this.url, relativeUrl, m, controller);
				if (!endpoint.getHttpMethod().equals(HttpMethod.OPTIONS.toString())) {
					result.addEnpoint(endpoint);
				}
			});
			response = Optional.of(result);
		}
		return response;
	}

	private String getRelativeUrl(Method m) {
		StringBuilder relativeUrl = new StringBuilder();
		// me quedo con la annotation (alguna de la lista)
		ReflectionUtils.filterRequestMappingAnnontations(m).findFirst().ifPresent(annotation -> {
			Optional<Object> value = Optional.ofNullable(AnnotationUtils.getValue(annotation));
			value.ifPresent(v -> relativeUrl.append(Arrays.asList((String[]) v).stream().findFirst().orElse("")));
		});
		return relativeUrl.toString();
	}

	private void getRealController() {
		this.controller = getRealObject(this.controller);
	}

	private Object getRealObject(Object object) {
		Object result = object;
		if (AopUtils.isAopProxy(object)) {
			try {
				LOGGER.debug("{} spring proxy, get real object", object);
				result = ((Advised) object).getTargetSource().getTarget();
				LOGGER.debug("Real Object: {}", object);
			} catch (Exception e) {
				LOGGER.error("Problemas al obtener el controller: {}", object, e);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
