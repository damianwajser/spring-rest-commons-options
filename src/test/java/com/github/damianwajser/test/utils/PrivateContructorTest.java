package com.github.damianwajser.test.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.damianwajser.config.WebMvcConfiguration;
import com.github.damianwajser.model.details.response.DetailFieldResponseFactory;
import com.github.damianwajser.utils.JsonSchemmaUtils;

@ContextConfiguration(classes = { WebMvcConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class PrivateContructorTest {

	@Test
	@org.junit.jupiter.api.Test
	public void detailFieldResponseFactoryTest() throws Exception {
		privateConstructor(DetailFieldResponseFactory.class);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void jsonSchemmaUtilsTest() throws Exception {
		privateConstructor(JsonSchemmaUtils.class);
	}

	private void privateConstructor(Class<?> clazz) throws Exception {
		Constructor<?> constructor = clazz.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		assertThatThrownBy(()->constructor.newInstance()).isInstanceOf(IllegalAccessException.class);
	}
}
