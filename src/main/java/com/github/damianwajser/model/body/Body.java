package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;

public abstract class Body {
	
	@JsonIgnore
	private Method method;
	@JsonIgnore
	private Class<?> parametrizedClass;

	@JsonUnwrapped
	private List<DetailField> fields = new ArrayList<>();
	
	private JsonSchema jsonSchema;
	
	public Body(Method m, Class<?> controllerClass) {
		this.method = m;
		this.parametrizedClass = controllerClass;
		this.fields = this.buildFields();
		this.setJsonSchema(fillJsonSchema());
	}

	protected abstract JsonSchema fillJsonSchema();

	protected abstract List<DetailField> buildFields();

	public List<DetailField> getFields() {
		return fields;
	}

	protected Method getMethod() {
		return this.method;
	}

	protected Class<?> getParametrizedClass() {
		return this.parametrizedClass;
	}

	public JsonSchema getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(JsonSchema jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

}
