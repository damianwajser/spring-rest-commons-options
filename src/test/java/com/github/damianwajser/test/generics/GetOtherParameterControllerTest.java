package com.github.damianwajser.test.generics;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.JsonBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.generics.OtherParameterController;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;
import com.github.damianwajser.utils.TestUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class GetOtherParameterControllerTest {

	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		JsonBuilder builder = new JsonBuilder(new OtherParameterController());
		OptionsResult result = builder.build().get();
		assertEquals("/other/parameter", result.getBaseUrl());
		List<Endpoint> endpoints = result.getEnpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/other/parameter", endpoint.getBaseUrl());
			checkAllGet(endpoint);
		}
	}

	private void checkAllGet(Endpoint endpoint) {
		List<DetailField> realField = null;
		System.out.println("checkeando: " + endpoint.getEndpoint());
		switch (endpoint.getEndpoint()) {
		case "GET - /other/parameter/{id}":
			TestUtils.checkTestPathId(endpoint);
			realField = endpoint.getBodyResponse().getFields();
			assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			break;
		case "GET - /other/parameter?code={String}":
			assertEquals(endpoint.getQueryString().getParams().size(), 1);
			TestUtils.checkParam(endpoint.getQueryString().getParams().get(0), "String", true, "code");
		case "GET - /other/parameter/":
		case "GET - /other/parameter/active":
		case "GET - /other/parameter/inactive":
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
		TestUtils.checkOtherParameter(realField);
		assertEquals("GET", endpoint.getHttpMethod());
	}

}
