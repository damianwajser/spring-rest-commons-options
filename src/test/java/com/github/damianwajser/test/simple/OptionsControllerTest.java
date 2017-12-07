package com.github.damianwajser.test.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OptionsControllerTest {

	public OptionsControllerTest() {
	}

	@Test
	public void testCreationOfANewProjectSucceeds() throws Exception {
		System.out.println("hacer test");
		// System.out.println(this.restTemplate.exchange("http://localhost:" + port +
		// "/other/parameter", HttpMethod.OPTIONS, null, CollectionResources.class));
	}
}
