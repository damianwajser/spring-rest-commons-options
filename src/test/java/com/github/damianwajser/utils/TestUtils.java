package com.github.damianwajser.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;

public class TestUtils {
	public static void checkParam(Parameters p, String type, boolean isRequired, String name) {
		assertNotNull(p);
		assertEquals(type, p.getType());
		assertEquals(p.getName(), name);
		assertEquals(isRequired, p.isRequired());
	}

	public static void checkField(DetailField realField, String name, String type) {
		assertEquals(name, realField.getName());
		assertEquals(type, realField.getType());
		assertFalse(realField.isAuditable());
	}

	public static void checkRequestBodyEmpty(Endpoint endpoint) {
		assertEquals(endpoint.getBodyRequest().getFields(), Collections.emptyList());
	}

	public static void checkTestPathId(Endpoint endpoint) {
		assertEquals("/{id}", endpoint.getRelativeUrl());
		assertEquals(1, endpoint.getPathVariable().getParams().size());
		TestUtils.checkParam(endpoint.getPathVariable().getParams().get(0), "Integer", true, "id");
	}

	public static void checkOtherParameter(List<DetailField> realField) {
		assertEquals(13, realField.size());
		checkField(realField.get(0), "active", "boolean");
//		checkField(realField.get(1), "processingCode", "String");
//		checkField(realField.get(2), "transactionType", "Integer");
//		checkField(realField.get(3), "msName", "Integer");
	}
	
	public static void checkPojoFields(List<DetailField> realField) {
		assertEquals(2, realField.size());
		checkField(realField.get(0), "notBlank", "String");
		checkField(realField.get(1), "visible", "boolean");

	}

	public static void checkPojoFieldsValidation(List<DetailField> realField) {
		checkPojoFields(realField);
		DetailFieldWithValidations notBlank = (DetailFieldWithValidations) realField.get(0);
		assertEquals(1, notBlank.getValidation().size());
		assertEquals("validacion", notBlank.getValidation().get(0).getMessageStr());

	}
}
