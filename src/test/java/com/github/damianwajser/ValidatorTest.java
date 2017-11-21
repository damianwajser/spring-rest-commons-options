package com.github.damianwajser;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
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
	public void contextLoads() throws Exception {
		for (Field f:Pojo.class.getDeclaredFields()) {
			Optional<List<Validator>> optValidators = ValidatorFactory.getValidations(f);
			System.out.println(optValidators);
			List<Validator> validators = optValidators.get();
			assertNotNull("no se encontraron validators", validators);
			
		}

	}

}