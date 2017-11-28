package com.github.damianwajser.test.simple;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.JsonBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.simple.GetPojoController;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;
import com.github.damianwajser.utils.TestUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class GetPojoControllerTest {

	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		JsonBuilder builder = new JsonBuilder(new GetPojoController());
		OptionsResult result = builder.build().get();
		assertEquals("/test123", result.getBaseUrl());
		List<Endpoint> endpoints = result.getEnpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/test123", endpoint.getBaseUrl());
			checkAllGet(endpoint);
		}
	}

	private void checkAllGet(Endpoint endpoint) {
		List<DetailField> realField = null;
		System.out.println("checkeando: " + endpoint.getEndpoint());
		switch (endpoint.getEndpoint()) {
		case "GET - /test123/{id}":
			TestUtils.checkTestPathId(endpoint);
			realField = endpoint.getBodyResponse().getFields();
			assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			assertEquals(1, endpoint.getHeaders().size());
			assertEquals("Authorization", endpoint.getHeaders().get(0).getName());
			assertEquals("String", endpoint.getHeaders().get(0).getType());
			break;
		case "GET - /test123?arg0={Integer}&arg1={String}":
			assertEquals(endpoint.getQueryString().getParams().size(), 2);
			TestUtils.checkParam(endpoint.getQueryString().getParams().get(0), "Integer", true, "arg0");
			TestUtils.checkParam(endpoint.getQueryString().getParams().get(1), "String", true, "arg1");
		case "GET - /test123":
			assertEquals("", endpoint.getRelativeUrl());
			assertEquals(Collections.EMPTY_LIST, endpoint.getPathVariable().getParams());
			assertEquals(1, endpoint.getBodyResponse().getFields().size());
			realField = endpoint.getBodyResponse().getFields();
			DetailFieldCollection field1 = (DetailFieldCollection) endpoint.getBodyResponse().getFields().iterator()
					.next();
			assertEquals("collection", field1.getType());
			realField = field1.getCollection();
			break;
		}
		TestUtils.checkRequestBodyEmpty(endpoint);
		TestUtils.checkPojoFields(realField, true);
		assertEquals("GET", endpoint.getHttpMethod());
	}

}
