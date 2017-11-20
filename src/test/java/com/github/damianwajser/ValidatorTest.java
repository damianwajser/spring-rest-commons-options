package com.github.damianwajser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.github.damianwajser.model.Pojo;
import com.github.damianwajser.model.validators.Validator;
import com.github.damianwajser.model.validators.ValidatorFactory;

public class ValidatorTest {

	@Test
	public void contextLoads() throws Exception {
		for (Field f:Pojo.class.getDeclaredFields()) {
			Optional<List<Validator>> optValidators = ValidatorFactory.getValidations(f);
//			List<Validator> validators = optValidators.get();
//			assertNotNull(validators, "no se encontraron validators");
			
		}

	}

}