package com.github.damianwajser.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.ResorucesBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.simple.ModifyPojoController;
import com.github.damianwajser.controllers.simple.PageableController;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Resource;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class PageableControllerTest {
	
	@Test
	@org.junit.jupiter.api.Test
	public void testGetAll() throws Exception {
		ResorucesBuilder builder = ResorucesBuilder.getInstance();
		builder.build(Arrays.asList(new PageableController()));
		CollectionResources resources = builder.getResources();
		assertNotNull(resources);
		Resource r = resources.getResource("/pageable");
		// assertEquals("404", result.getHttpCodes().get(404).get(1));
		List<Endpoint> endpoints = r.getEndpoints();
		for (int i = 0; i < endpoints.size(); i++) {
			Endpoint endpoint = endpoints.get(i);
			assertEquals("/pageable", endpoint.getBaseUrl());

		}
	}
}
