package com.github.damianwajser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.JsonBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.PojoController;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;

@ContextConfiguration(classes= {WebMvcConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PojoControllerTest {

	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		JsonBuilder builder = new JsonBuilder(new PojoController());
		OptionsResult result = builder.build().get();
		assertEquals("/test123", result.getBaseUrl());
		List<Endpoint> endpoints = result.getEnpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/test123", endpoint.getBaseUrl());
			checkAllGet(endpoint);
		}
	}

	private DetailField checkAllGet(Endpoint endpoint) {
		DetailField realField = null;
		System.out.println("checkeando: " + endpoint.getEndpoint());
		switch (endpoint.getEndpoint()) {
		case "GET - /test123/":
			assertEquals("/", endpoint.getRelativeUrl());
			assertEquals(Collections.EMPTY_LIST, endpoint.getPathVariable().getParams());
			DetailFieldCollection field = (DetailFieldCollection) endpoint.getBodyResponse().getFields().iterator()
					.next();
			assertEquals("collection", field.getType());
			realField = field.getCollection().iterator().next();
			assertEquals (Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			break;
		case "GET - /test123/{id}":
			assertEquals("/{id}", endpoint.getRelativeUrl());
			assertEquals(1, endpoint.getPathVariable().getParams().size());
			checkParam(endpoint.getPathVariable().getParams().get(0), "Integer", true,"id");
			assertEquals(1, endpoint.getBodyResponse().getFields().size());
			realField = endpoint.getBodyResponse().getFields().iterator().next();
			assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			break;
		case "GET - /test123/?arg0={Integer}&arg1={String}":
			assertEquals("/", endpoint.getRelativeUrl());
			assertEquals(Collections.EMPTY_LIST, endpoint.getPathVariable().getParams());
			assertEquals(1, endpoint.getBodyResponse().getFields().size());
			realField = endpoint.getBodyResponse().getFields().iterator().next();
			DetailFieldCollection field1 = (DetailFieldCollection) endpoint.getBodyResponse().getFields().iterator()
					.next();
			assertEquals("collection", field1.getType());
			realField = field1.getCollection().iterator().next();
			assertEquals(endpoint.getQueryString().getParams().size(), 2);
			checkParam(endpoint.getQueryString().getParams().get(0), "Integer", true,"arg0");
			checkParam(endpoint.getQueryString().getParams().get(1), "String", true,"arg1");
			break;
		}
		checkRequestBodyEmpty(endpoint);
		assertEquals("GET", endpoint.getHttpMethod());
		checkPojoField(realField);
		assertFalse(realField.isAuditable());
		return realField;
	}

	private void checkParam(Parameters p, String type, boolean isRequired, String name) {
		assertNotNull(p);
		assertEquals(type, p.getType());
		assertEquals(p.getName(), name);
		assertEquals(isRequired, p.isRequired());
	}
	private void checkPojoField(DetailField realField) {
		assertEquals("notBlank", realField.getName());
		assertEquals("String", realField.getType());
	}

	private void checkRequestBodyEmpty(Endpoint endpoint) {
		assertEquals(endpoint.getBodyRequest().getFields(), Collections.emptyList());
	}
}
