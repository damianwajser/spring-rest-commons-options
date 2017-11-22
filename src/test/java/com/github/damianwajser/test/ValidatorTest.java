package com.github.damianwajser.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.model.Pojo;
import com.github.damianwajser.model.validators.Validator;
import com.github.damianwajser.model.validators.ValidatorFactory;

@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorTest {

	@Test
	@org.junit.jupiter.api.Test
	public void contextLoads() throws Exception {
		Optional<List<Validator>> optValidators = ValidatorFactory.getValidations(Pojo.class.getDeclaredField("notBlank"));

		List<Validator> validators = optValidators.get();
		assertNotNull("no se encontraron validators", validators);
	}

}