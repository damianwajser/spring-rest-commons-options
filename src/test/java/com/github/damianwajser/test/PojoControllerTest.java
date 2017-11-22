package com.github.damianwajser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.JsonBuilder;
import com.github.damianwajser.controllers.PojoController;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.OptionsResult;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;

@RunWith(SpringJUnit4ClassRunner.class)
public class PojoControllerTest {
	
	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		JsonBuilder builder = new JsonBuilder(new PojoController());
		OptionsResult result = builder.build().get();
		assertEquals("/test123", result.getBaseUrl());
		Endpoint endpoint = result.getEnpoints().get(0);
		assertEquals("/test123", endpoint.getBaseUrl());
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/", endpoint.getRelativeUrl());
		assertEquals(endpoint.getBodyRequest().getFields(), Collections.emptyList());
		DetailFieldCollection field = (DetailFieldCollection)endpoint.getBodyResponse().getFields().iterator().next();
		assertEquals("collection", field.getType());
		assertEquals("notBlank", field.getCollection().iterator().next().getName());
		assertEquals("String", field.getCollection().iterator().next().getType());
	
	}
}
