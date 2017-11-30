package com.github.damianwajser.builders.json;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.controller.OptionsController;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.details.response.DetailFieldResponseFactory;
import com.github.damianwajser.model.details.strategys.DetailFieldStrategy;
import com.github.damianwajser.utils.ReflectionUtils;

public class ResorucesBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResorucesBuilder.class);

	private static ResorucesBuilder instance;
	private List<Object> controllers;
	private CollectionResources resources;

	private ResorucesBuilder() {
	}

	public static synchronized ResorucesBuilder getInstance() {
		if (instance == null) {
			synchronized (ResorucesBuilder.class) {
				if (instance == null) {
					instance = new ResorucesBuilder();
				}
			}
		}
		return instance;
	}

	private void init() {
		this.controllers = new ArrayList<>();
		this.resources = new CollectionResources();
	}
	public void build(ApplicationContext context) {
		init(context);
		buildEndpoints(this.controllers);
	}
	public void build(List<Object> controllers) {
		init();
		this.controllers = controllers;
		buildEndpoints(controllers);	
	}
	private void buildEndpoints(Iterable<Object> controllers) {
		controllers.forEach(c -> {
			List<Method> methods = Arrays.asList(c.getClass().getMethods()).stream()
					.filter(m -> ReflectionUtils.containsRequestAnnotation(m)
							&& !m.getDeclaringClass().getPackage().getName()
									.startsWith("org.springframework.boot.autoconfigure.web")
							&& !m.getDeclaringClass().equals(OptionsController.class))
					.collect(Collectors.toList());
			methods.forEach(m -> {
				String relativeUrl = getRelativeUrl(m);
				Endpoint endpoint = new Endpoint(relativeUrl, m, c);
				if (!endpoint.getHttpMethod().equals(HttpMethod.OPTIONS.toString())) {
					LOGGER.debug("se crearon los siguientes Endpoints: {}", endpoint);
					resources.addEndpoint(endpoint);
				}
			});
		});

	}

	private String getRelativeUrl(Method m) {
		StringBuilder relativeUrl = new StringBuilder();
		// me quedo con la annotation (alguna de la lista)
		ReflectionUtils.filterRequestMappingAnnontations(m).findFirst().ifPresent(annotation -> {
			Optional<String[]> value;
			if (RequestMapping.class.isAssignableFrom(annotation.getClass())) {
				RequestMapping a = (RequestMapping) annotation;
				value = Optional.ofNullable(a.path().length == 0 ? a.value() : a.path());
			} else {
				value = Optional.ofNullable((String[]) AnnotationUtils.getValue(annotation));
			}
			value.ifPresent(v -> relativeUrl.append(Arrays.asList((String[]) v).stream().findFirst().orElse("")));
		});
		return relativeUrl.toString();
	}

	private void init(ApplicationContext context) {
		init();
		LOGGER.debug("Get All ExceptionHandlers");
		List<Object> exceptionsHandlers = ReflectionUtils
				.proxyToObject(context.getBeansWithAnnotation(ControllerAdvice.class).values());
		LOGGER.debug("Get All RestController");
		exceptionsHandlers.forEach(h -> buildHttpCodes(h));
		controllers
				.addAll(ReflectionUtils.proxyToObject(context.getBeansWithAnnotation(RestController.class).values()));
		LOGGER.debug("Get All Controller");
		controllers.addAll(ReflectionUtils.proxyToObject(context.getBeansWithAnnotation(Controller.class).values()));
	}

	private void buildHttpCodes(Object h) {
		Arrays.asList(h.getClass().getDeclaredMethods()).stream().filter(
				m -> m.isAnnotationPresent(ExceptionHandler.class) && m.isAnnotationPresent(ResponseStatus.class))
				.forEach(m -> {
					ResponseStatus status = m.getAnnotation(ResponseStatus.class);
					Type returnType = m.getGenericReturnType();
					DetailFieldStrategy strategy = null;
					strategy = DetailFieldResponseFactory.getCreationStrategy(returnType, m.getDeclaringClass());
					this.resources.addHttpCode(status.value().value(), strategy.createDetailField(false));
				});
	}

	public CollectionResources getResources() {
		return this.resources;
	}
	public SortedSet<String> getEnpoints() {
		return this.resources.getEndpointsList();
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
