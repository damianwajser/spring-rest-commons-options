package com.github.damianwajser.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.damianwajser.builders.OptionBuilder;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.utils.StringUtils;

@RestController
public class OptionsController implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ApplicationContext context;

	private Map<String, OptionsResult> controllers = new HashMap<>();

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
	public OptionsResult handleResults(HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
		String path = StringUtils.deleteIfEnd(request.getServletPath(), "/");
		return Optional.ofNullable(controllers.get(path))
				.orElseThrow(() -> new HttpRequestMethodNotSupportedException("OPTIONS"));
	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	public Set<String> handleResults() {
		return controllers.keySet();

	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		Map<String, Object> beans = context.getBeansWithAnnotation(RestController.class);
		beans.putAll(context.getBeansWithAnnotation(Controller.class));
		beans.forEach((k, v) -> addController(v));
	}

	private void addController(Object v) {
		String packageName = v.getClass().getPackage().getName();
		if (!packageName.startsWith("org.springframework.boot.autoconfigure.web")) {
			OptionsResult result = new OptionBuilder(v).build();
			controllers.put(result.getBaseUrl(), result);
		}
	}
}
