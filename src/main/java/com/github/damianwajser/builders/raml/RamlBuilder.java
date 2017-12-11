package com.github.damianwajser.builders.raml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.damianwajser.builders.raml.model.BodyRaml;
import com.github.damianwajser.builders.raml.model.QueryParameterRaml;
import com.github.damianwajser.builders.raml.model.ResourceMethodRaml;
import com.github.damianwajser.builders.raml.model.ResourceRaml;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.Resource;

public class RamlBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(RamlBuilder.class);

	private CollectionResources resources;

	public RamlBuilder(CollectionResources resources) {
		this.resources = resources;
	}

	public Raml build() {
		return getResourceRaml(resources);
	}

	private Raml getResourceRaml(CollectionResources resources) {
		Raml raml = new Raml("e-BookMobile API", "http://api.e-bookmobile.com/{version}", "v1");
		ResourceRaml root = new ResourceRaml();
		resources.getResources().forEach((path, resource) -> getEndpointsRaml(path, root, resource));
		raml.setResources(root);
		return raml;

	}

	private void getEndpointsRaml(String path, ResourceRaml root, Resource resource) {
		ResourceRaml raml = new ResourceRaml();
		resource.getEndpoints().forEach(e -> {
			List<Parameters> params = e.getPathVariable().getParams();
			if (params != null && !params.isEmpty()) {
				raml.add("/{" + params.get(0).getName() + "}", getMethods(e));
			} else if (e.getRelativeUrl().equals("/") || e.getRelativeUrl().isEmpty()) {
				LOGGER.info("agregando el path: {}, relative: {}", path, e.getRelativeUrl());
				root.add(path, getMethods(e));
			} else {
				LOGGER.info("agregando el path: {}, relative: {}", path, e.getRelativeUrl());
				raml.add(e.getRelativeUrl(), getMethods(e));
			}
		});
		root.add(path, raml);
	}

	private ResourceRaml getMethods(Endpoint e) {
		ResourceRaml methods = new ResourceRaml();
		ResourceRaml qs = buildQueryString(e);
		if (qs.getResource() != null && !qs.getResource().isEmpty())
			methods.add(e.getHttpMethod(), qs);
		methods.add(e.getHttpMethod(), buildBody(e));
		return methods;
	}

	private ResourceRaml buildBody(Endpoint e) {
		ResourceRaml body = new ResourceRaml();
		ResourceRaml bodyResponse = new ResourceRaml();
		ResourceRaml bodyRaml = new ResourceRaml();
		bodyRaml.add("body", getRealBody(e));
		body.add("200", bodyRaml);
		bodyResponse.add("responses", body);
		return bodyResponse;
	}

	private ResourceRaml getRealBody(Endpoint e) {
		ResourceRaml body = new ResourceRaml();
		BodyRaml bodyRaml = new BodyRaml();
		bodyRaml.setJsonSchema(e.getBodyResponse().getJsonSchema());
		body.add("application/json", bodyRaml);
		return body;
	}

	private ResourceRaml buildQueryString(Endpoint e) {
		ResourceMethodRaml m = new ResourceMethodRaml();
		e.getQueryString().getParams().forEach(qs -> m.addQueryParameters(qs.getName(),
				new QueryParameterRaml(qs.getName(), qs.getType(), "", qs.isRequired())));
		return m;
	}
}
