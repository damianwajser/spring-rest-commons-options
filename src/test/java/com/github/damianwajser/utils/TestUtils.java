package com.github.damianwajser.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.hibernate.validator.constraints.NotBlank;

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
		checkField(realField, name, type, false);
	}
	
	public static void checkField(DetailField realField, String name, String type, boolean auditable) {
		assertEquals(name, realField.getName());
		assertEquals(type, realField.getType());
		assertEquals(realField.isAuditable(), auditable);
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
		int i=0;
		checkField(realField.get(i++), "active", "boolean");
		checkField(realField.get(i++), "code", "String");
		checkField(realField.get(i++), "createDate", "Date", true);
		checkField(realField.get(i++), "createDateStr", "String");
		checkField(realField.get(i++), "createUser", "String", true);
		checkField(realField.get(i++), "dateStr", "String");
		checkField(realField.get(i++), "id", "Integer");
		checkField(realField.get(i++), "messageType", "String");
		checkField(realField.get(i++), "modifyDate", "Date");
		checkField(realField.get(i++), "modifyUser", "String", true);
		checkField(realField.get(i++), "msName", "String");
		checkField(realField.get(i++), "processingCode", "String");
		checkField(realField.get(i++), "transactionType", "Integer");
//		checkField(realField.get(i++), "version", "Long");
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

	public static void checkOtherParameterWithValidation(List<DetailField> requestField) {
		checkOtherParameter(requestField);
		int i=0;
		assertEquals(DetailField.class, requestField.get(i++).getClass());
//		checkField(realField.get(i++), "active", "boolean");
//		checkField(realField.get(i++), "code", "String");
//		checkField(realField.get(i++), "createDate", "Date", true);
//		checkField(realField.get(i++), "createDateStr", "String");
//		checkField(realField.get(i++), "createUser", "String", true);
//		checkField(realField.get(i++), "dateStr", "String");
//		checkField(realField.get(i++), "id", "Integer");
//		checkField(realField.get(i++), "messageType", "String");
//		checkField(realField.get(i++), "modifyDate", "Date");
//		checkField(realField.get(i++), "modifyUser", "String", true);
//		checkField(realField.get(i++), "msName", "String");
//		checkField(realField.get(i++), "processingCode", "String");
//		checkField(realField.get(i++), "transactionType", "Integer");
	}
}