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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.damianwajser.model.Endpoint;
import com.github.damianwajser.model.Parameters;
import com.github.damianwajser.model.details.DetailField;
import com.github.damianwajser.model.details.DetailFieldWithValidations;
import com.github.damianwajser.model.validators.impl.PatternValidator;

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

	public static void checkOtherParameterResponse(List<DetailField> realField) {
		assertEquals(13, realField.size());
		int i = 0;
		checkField(realField.get(i++), "active", "boolean");
		checkField(realField.get(i++), "code", "String");
		checkField(realField.get(i++), "createDate", "Date", true);
		checkField(realField.get(i++), "createDateStr", "String");
		checkField(realField.get(i++), "createUser", "String", true);
		checkField(realField.get(i++), "dateStr", "String");
		checkField(realField.get(i++), "id", "Integer");
		checkField(realField.get(i++), "messageType", "String");
		checkField(realField.get(i++), "modifyDate", "Date", true);
		checkField(realField.get(i++), "modifyUser", "String", true);
		checkField(realField.get(i++), "msName", "String");
		checkField(realField.get(i++), "processingCode", "String");
		checkField(realField.get(i++), "transactionType", "Integer");
		// checkField(realField.get(i++), "version", "Long");
	}

	public static void checkOtherParameterRequest(List<DetailField> realField) {
		assertEquals(9, realField.size());
		int i = 0;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "active", "boolean");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "code", "String");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "createDateStr", "String");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "dateStr", "String");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "id", "Integer");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "messageType", "String");

		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "msName", "String");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "processingCode", "String");
		i++;
		System.out.println(realField.get(i).getName());
		checkField(realField.get(i), "transactionType", "Integer");
		assertEquals(8, i);
	}

	public static void checkPojoFields(List<DetailField> realField, boolean isRequest) {
		assertEquals(5, realField.size());
		if (isRequest) {
			checkField(realField.get(0), "notBlank", "String");
			checkField(realField.get(1), "property", "String");
			checkField(realField.get(2), "set", "String");
			checkField(realField.get(3), "testGetJson", "String");
		}else {
			checkField(realField.get(0), "get", "String");
			checkField(realField.get(1), "notBlank", "String");
			checkField(realField.get(2), "property", "String");
			checkField(realField.get(3), "testSetJson", "String");
		}
		checkField(realField.get(4), "visible", "boolean");
	}

	public static void checkPojoFieldsValidation(List<DetailField> realField) {
		checkPojoFields(realField, false);
		DetailFieldWithValidations notBlank = (DetailFieldWithValidations) realField.get(1);
		assertEquals(1, notBlank.getValidation().size());
		assertEquals("validacion", notBlank.getValidation().get(0).getMessageStr());

	}

	public static void checkOtherParameterWithValidation(List<DetailField> requestField) {
		checkOtherParameterRequest(requestField);
		int i = 0;
		DetailField active = requestField.get(i++);
		assertEquals(DetailField.class, active.getClass());

		DetailFieldWithValidations code = (DetailFieldWithValidations) requestField.get(i++);
		assertEquals(1, code.getValidation().size());
		assertEquals("El campo code es obligatorio", code.getValidation().get(0).getMessageStr());

		DetailField createDateStr = requestField.get(i++);
		assertEquals(DetailField.class, createDateStr.getClass());

		DetailField dateStr = requestField.get(i++);
		assertEquals(DetailField.class, dateStr.getClass());

		DetailField id = requestField.get(i++);
		assertEquals(DetailField.class, id.getClass());

		DetailFieldWithValidations messageType = (DetailFieldWithValidations) requestField.get(i++);
		assertEquals(2, messageType.getValidation().size());
		assertEquals("El campo messageType es obligatorio", messageType.getValidation().get(0).getMessageStr());
		PatternValidator messageTypeValidator = (PatternValidator) messageType.getValidation().get(1);
		assertEquals("Valor invalido de messageType", messageTypeValidator.getMessageStr());
		assertEquals("(200)|(400)", messageTypeValidator.getRegularExpression());

		DetailFieldWithValidations msName = (DetailFieldWithValidations) requestField.get(i++);
		assertEquals(1, msName.getValidation().size());
		assertEquals("El campo msName es obligatorio", msName.getValidation().get(0).getMessageStr());

		DetailFieldWithValidations processingCode = (DetailFieldWithValidations) requestField.get(i++);
		assertEquals(2, processingCode.getValidation().size());
		assertEquals("El campo processingCode es obligatorio", processingCode.getValidation().get(0).getMessageStr());
		PatternValidator processingCodeValidator = (PatternValidator) processingCode.getValidation().get(1);
		assertEquals("Valor invalido de processingCode", processingCodeValidator.getMessageStr());
		assertEquals("(000000)|(020000)|(200000)|(220000)", processingCodeValidator.getRegularExpression());

		DetailFieldWithValidations transactionType = (DetailFieldWithValidations) requestField.get(i);
		assertEquals(1, transactionType.getValidation().size());
		assertEquals("El campo transactionType es obligatorio", transactionType.getValidation().get(0).getMessageStr());
		assertEquals(8, i);
	}
}
