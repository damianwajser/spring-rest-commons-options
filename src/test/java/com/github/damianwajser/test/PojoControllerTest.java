package com.github.damianwajser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		switch (endpoint.getEndpoint()) {
		case "GET - /test123/":
			assertEquals("/", endpoint.getRelativeUrl());
			assertEquals(endpoint.getPathVariable().getParams(), Collections.EMPTY_LIST);
			DetailFieldCollection field = (DetailFieldCollection) endpoint.getBodyResponse().getFields().iterator()
					.next();
			assertEquals("collection", field.getType());
			realField = field.getCollection().iterator().next();
			break;
		case "GET - /test123/{id}":
			assertEquals("/{id}", endpoint.getRelativeUrl());
			assertEquals(1, endpoint.getPathVariable().getParams().size());
			Parameters path = endpoint.getPathVariable().getParams().iterator().next();
			assertEquals(path.getName(), "id");
			assertEquals(path.getType(), "Integer");
			assertTrue(path.isRequired());
			assertEquals(1, endpoint.getBodyResponse().getFields().size());
			realField = endpoint.getBodyResponse().getFields().iterator().next();
			break;
		}
		checkRequestBodyEmpty(endpoint);
		assertEquals("GET", endpoint.getHttpMethod());
		checkPojoField(realField);
		assertFalse(realField.isAuditable());
		assertEquals(endpoint.getQueryString().getParams(), Collections.EMPTY_LIST);
		return realField;
	}

	private void checkPojoField(DetailField realField) {
		assertEquals("notBlank", realField.getName());
		assertEquals("String", realField.getType());
	}

	private void checkRequestBodyEmpty(Endpoint endpoint) {
		assertEquals(endpoint.getBodyRequest().getFields(), Collections.emptyList());
	}
}
