package com.github.damianwajser.test.simple;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptionsControllerTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	public OptionsControllerTest() {
	}

	public void testCreationOfANewProjectSucceeds() throws Exception {
		// System.out.println(this.restTemplate.exchange("http://localhost:" + port +
		// "/other/parameter", HttpMethod.OPTIONS, null, CollectionResources.class));
	}
}
