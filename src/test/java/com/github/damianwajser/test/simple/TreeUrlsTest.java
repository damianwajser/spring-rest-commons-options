package com.github.damianwajser.test.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.config.WebMvcConfiguration;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class TreeUrlsTest {

	@Test
	@org.junit.jupiter.api.Test
	public void test() throws Exception {
//		JsonBuilder builder = new JsonBuilder(new TreeUrlsController());
//		OptionsResult result = builder.build().get();
//		assertEquals("/", result.getBaseUrl());
//		assertEquals("/ypf", result.getResource());
//		assertEquals("/ypf", result.getResource());
	}
}
