package com.github.damianwajser.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.ResourcesBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.simple.ModifyPojoController;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Resource;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.utils.TestUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ModifyPojoControllerTest {

	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		ResourcesBuilder builder = ResourcesBuilder.getInstance();
		builder.build(Arrays.asList(new ModifyPojoController()));
		CollectionResources resources = builder.getResources();
		assertNotNull(resources);
		Resource r = resources.getResource("/test1234");
		// assertEquals("404", result.getHttpCodes().get(404).get(1));
		List<Endpoint> endpoints = r.getEndpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/test1234", endpoint.getBaseUrl());
			checkAll(endpoint);

		}
	}

	private void checkAll(Endpoint endpoint) {
		List<DetailField> responseField = null;
		List<DetailField> requestField = null;
		System.out.println("checkeando: " + endpoint.getEndpoint());
		switch (endpoint.getEndpoint()) {
		case "PUT - /test1234/{id}":
			TestUtils.checkTestPathId(endpoint);
			assertEquals("PUT", endpoint.getHttpMethod());
		case "POST - /test1234":
			responseField = endpoint.getBodyResponse().getFields();
			requestField = endpoint.getBodyRequest().getFields();
			assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
			break;
		}
		TestUtils.checkPojoFieldsValidation(requestField);
	}

}
