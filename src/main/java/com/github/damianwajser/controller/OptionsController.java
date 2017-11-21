package com.github.damianwajser.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.builders.json.JsonBuilder;
import com.github.damianwajser.builders.raml.RamlBuilder;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.utils.StringUtils;

@RestController
public class OptionsController implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OptionsController.class);

	@Autowired
	private ApplicationContext context;

	private static Map<String, OptionsResult> controllers = new HashMap<>();

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS, produces = "application/json")
	public OptionsResult handleResultsJson(HttpServletRequest request, @RequestParam("method") Optional<String> method)
			throws HttpRequestMethodNotSupportedException {
		String path = request.getServletPath().equals("/")?request.getServletPath():StringUtils.deleteIfEnd(request.getServletPath(), "/");
		LOGGER.info("solicitando JSON: {}", path);
		OptionsResult result = Optional.ofNullable(controllers.get(path))
				.orElseThrow(() -> new HttpRequestMethodNotSupportedException("OPTIONS"));
		if (method.isPresent()) {
			OptionsResult aux = new OptionsResult(result.getBaseUrl());
			BeanUtils.copyProperties(result, aux);
			aux.setEnpoints(result.getEnpoints().stream().filter(e -> e.getHttpMethod().equalsIgnoreCase(method.get()))
					.collect(Collectors.toList()));
			result = aux;
		}
		return result;
	}

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS, consumes = "application/x-yaml", produces = "application/x-yaml")
	public Object handleResultsYML(HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
		String path = StringUtils.deleteIfEnd(request.getServletPath(), "/");
		LOGGER.info("solicitando RAML: {}", path);
		return new RamlBuilder(controllers.get(path)).build();

	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	public Set<String> handleResults() {
		return controllers.keySet();
	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		try {
			LOGGER.info("Comenzando la creacion de documentacion");
			Map<String, Object> beans = context.getBeansWithAnnotation(RestController.class);
			LOGGER.debug("Get All Controllers");
			beans.putAll(context.getBeansWithAnnotation(Controller.class));
			beans.forEach((k, v) -> {
				if (!v.equals(this)) {
					new JsonBuilder(v).build().ifPresent(c -> {
						LOGGER.info("Add the controller for: " + c.getBaseUrl());
						controllers.put(c.getBaseUrl(), c);
					});
				}
			});
		} catch (Exception e) {
			LOGGER.error("problemas al crear la dcumentacion", e);
		}
	}
}
