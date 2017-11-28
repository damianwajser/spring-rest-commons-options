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
import com.github.damianwajser.controllers.simple.ModifyPojoController;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.utils.TestUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ModifyPojoControllerTest {

	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		JsonBuilder builder = new JsonBuilder(new ModifyPojoController());
		OptionsResult result = builder.build().get();
		assertEquals("/test123", result.getBaseUrl());
		List<Endpoint> endpoints = result.getEnpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/test123", endpoint.getBaseUrl());
			checkAll(endpoint);
		}
	}

	private void checkAll(Endpoint endpoint) {
		List<DetailField> responseField = null;
		List<DetailField> requestField = null;
		System.out.println("checkeando: " + endpoint.getEndpoint());
		switch (endpoint.getEndpoint()) {
		case "PUT - /test123/{id}":
			TestUtils.checkTestPathId(endpoint);
			assertEquals("PUT", endpoint.getHttpMethod());
		case "POST - /test123":
			responseField = endpoint.getBodyResponse().getFields();
			requestField = endpoint.getBodyRequest().getFields();
			assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			break;
		}
		TestUtils.checkPojoFieldsValidation(requestField);
	}

}
