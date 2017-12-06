package com.github.damianwajser.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.damianwajser.builders.json.ResorucesBuilder;
import com.github.damianwajser.controllers.simple.ModifyPojoController;
import com.github.damianwajser.controllers.simple.PageableController;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Resource;
import com.github.damianwajser.model.header.Header;
import com.github.damianwajser.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PageableControllerTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	private CollectionResources resources;

	@BeforeEach
	private void initJunit5() {
		ResorucesBuilder builder = ResorucesBuilder.getInstance();
		builder.build(Arrays.asList(new PageableController()));
		resources = builder.getResources();

	}

	@Before
	public void initJunit4() {
		ResponseEntity<CollectionResources> result = this.restTemplate.exchange(
				"http://localhost:" + port + "/pageable", HttpMethod.OPTIONS, null, CollectionResources.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(result.getBody());
		resources = result.getBody();
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testPageable() throws Exception {

		assertEquals(1, resources.getEndpointsList().size());
		Resource pageable = resources.getResource("/pageable");
		assertNotNull(pageable);

		checkEnpoints(pageable);

	}

	private void checkEnpoints(Resource pageable) {
		assertEquals(1, pageable.getEndpoints().size());
		Endpoint endpoint = pageable.getEndpoints().iterator().next();
		checkHeader(endpoint);
		checkBodyRequest(endpoint);
		checkBodyResponse(endpoint);
	}

	private void checkBodyResponse(Endpoint endpoint) {
		TestUtils.checkPojoFields(endpoint.getBodyResponse().getFields(), true);

	}

	private void checkBodyRequest(Endpoint endpoint) {
		TestUtils.checkRequestBodyEmpty(endpoint);
	}

	private void checkHeader(Endpoint endpoint) {
		assertEquals(1, endpoint.getHeaders().size());
		Header header = endpoint.getHeaders().get(0);
		assertEquals("Authorization", header.getName());
		assertEquals("String", header.getType());
	}
}
