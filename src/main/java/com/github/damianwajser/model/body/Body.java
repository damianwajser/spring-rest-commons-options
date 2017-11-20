package com.github.damianwajser.model.body;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.github.damianwajser.model.details.DetailField;

public abstract class Body {
	
	@JsonIgnore
	private Method method;
	@JsonIgnore
	private Class<?> controllerClass;

	@JsonUnwrapped
	private Collection<DetailField> fields = new ArrayList<>();
	
	private JsonSchema jsonSchema;
	
	public Body(Method m, Class<?> controllerClass) {
		this.method = m;
		this.controllerClass = controllerClass;
		this.fields = this.buildFields();
		this.setJsonSchema(fillJsonSchema());
	}

	protected abstract JsonSchema fillJsonSchema();

	protected abstract Collection<DetailField> buildFields();

	public Collection<DetailField> getFields() {
		return fields;
	}

	public void setFields(Collection<DetailField> fields) {
		this.fields = fields;
	}
	
	protected Method getMethod() {
		return this.method;
	}

	public Class<?> getControllerClass() {
		return this.controllerClass;
	}

	public JsonSchema getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(JsonSchema jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

}
