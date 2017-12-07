package com.github.damianwajser.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.builders.json.ResorucesBuilder;
import com.github.damianwajser.builders.raml.RamlBuilder;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.utils.StringUtils;

@RestController
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class OptionsController implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OptionsController.class);
	@Autowired
	private ApplicationContext context;

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS, produces = "application/json")
	public CollectionResources handleResultsJson(HttpServletRequest request,
			@RequestParam("method") Optional<String> method) {
		String path = request.getServletPath().equals("/") ? request.getServletPath()
				: StringUtils.deleteIfEnd(request.getServletPath(), "/");
		LOGGER.info("solicitando JSON: {}", path);
		CollectionResources resources = ResorucesBuilder.getInstance().getResources();
		return resources.filterPath(path);
	}

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS, produces = "application/x-yaml")
	public Object handleResultsYML(HttpServletRequest request) {
		CollectionResources resource = handleResultsJson(request, Optional.empty());
		return new RamlBuilder(resource).build();

	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	public Iterable<String> handleResults() {
		return ResorucesBuilder.getInstance().getEnpoints();
	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		try {
			LOGGER.info("Comenzando la creacion de documentacion");
			ResorucesBuilder.getInstance().build(context);
		} catch (Exception e) {
			LOGGER.error("problemas al crear la documentacion", e);
		}
	}
}
