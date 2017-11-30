package com.github.damianwajser.test.generics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.builders.json.ResorucesBuilder;
import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.controllers.generics.OtherParameterController;
import com.github.damianwajser.model.CollectionResources;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Resource;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldCollection;
import com.github.damianwajser.utils.TestUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class GetOtherParameterControllerTest {

	private List<Endpoint> endpoints;

	private static final int DELETE = 0;
	private static final int GET_ALL = 1;
	private static final int GET_ACTIVE = 2;
	private static final int GET_INACTIVE = 3;
	private static final int GET_RESPONSE = 4;
	private static final int GET_ID = 5;
	private static final int GET_BY_CODE = 6;
	private static final int POST = 7;
	private static final int PUT = 8;

	@Before
	@BeforeEach
	public void setup() {
		ResorucesBuilder builder = ResorucesBuilder.getInstance();
		builder.build(Arrays.asList(new OtherParameterController()));
		CollectionResources resources = builder.getResources();
		assertNotNull(resources);
		Resource r = resources.getResource("/other/parameter");
		endpoints = r.getEndpoints();
		assertEquals("POST", endpoints.get(POST).getHttpMethod());

	}

	@Test
	@org.junit.jupiter.api.Test
	public void testResponse() throws Exception {
		Endpoint endpoint = endpoints.get(GET_RESPONSE);
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		assertEquals("GET", endpoint.getHttpMethod());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals("/response", endpoint.getRelativeUrl());
		checkSingleResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testFindById() throws Exception {
		Endpoint endpoint = endpoints.get(GET_ID);
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		TestUtils.checkTestPathId(endpoint);
		checkSingleResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void delete() throws Exception {
		Endpoint endpoint = endpoints.get(DELETE);
		assertEquals("DELETE", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		TestUtils.checkTestPathId(endpoint);
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		assertEquals(Collections.EMPTY_LIST, endpoint.getBodyRequest().getFields());
		assertEquals(Collections.EMPTY_LIST, endpoint.getBodyResponse().getFields());
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testGetByCode() throws Exception {
		Endpoint endpoint = endpoints.get(GET_BY_CODE);
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals("", endpoint.getRelativeUrl());
		assertEquals(1, endpoint.getQueryString().getParams().size());
		TestUtils.checkParam(endpoint.getQueryString().getParams().get(0), "String", true, "code");
		checkCollectionResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testGetALL() throws Exception {
		Endpoint endpoint = endpoints.get(GET_ALL);
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals("/", endpoint.getRelativeUrl());
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		checkCollectionResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testGetInactive() throws Exception {
		Endpoint endpoint = endpoints.get(GET_INACTIVE);
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		assertEquals("/inactive", endpoint.getRelativeUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		checkCollectionResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testGetActive() throws Exception {
		Endpoint endpoint = endpoints.get(GET_ACTIVE);
		assertEquals("GET", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		assertEquals("/active", endpoint.getRelativeUrl());
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		checkCollectionResponse(endpoint);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testPost() throws Exception {
		Endpoint endpoint = endpoints.get(POST);
		System.out.println("checkeando: " + endpoint.getEndpoint());
		assertEquals("POST", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		assertEquals("", endpoint.getRelativeUrl());
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		checkSingleResponse(endpoint);
		TestUtils.checkOtherParameterWithValidation(endpoint.getBodyRequest().getFields());
	}

	@Test
	@org.junit.jupiter.api.Test
	public void testPut() throws Exception {
		Endpoint endpoint = endpoints.get(PUT);
		System.out.println("checkeando: " + endpoint.getEndpoint());
		TestUtils.checkTestPathId(endpoint);
		assertEquals("PUT", endpoint.getHttpMethod());
		assertEquals("/other/parameter", endpoint.getBaseUrl());
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		checkSingleResponse(endpoint);
		TestUtils.checkOtherParameterWithValidation(endpoint.getBodyRequest().getFields());
	}

	private void checkSingleResponse(Endpoint endpoint) {
		assertEquals(Collections.EMPTY_LIST, endpoint.getQueryString().getParams());
		List<DetailField> responseField = endpoint.getBodyResponse().getFields();
		TestUtils.checkOtherParameterResponse(responseField);
	}

	private void checkCollectionResponse(Endpoint endpoint) {
		assertEquals(Collections.EMPTY_LIST, endpoint.getPathVariable().getParams());
		assertEquals(1, endpoint.getBodyResponse().getFields().size());
		DetailFieldCollection field1 = (DetailFieldCollection) endpoint.getBodyResponse().getFields().iterator().next();
		assertEquals("collection", field1.getType());
		TestUtils.checkOtherParameterResponse(field1.getCollection());
	}
}
