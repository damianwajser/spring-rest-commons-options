package com.github.damianwajser.builders.raml.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

public class BodyRaml {

	@JsonProperty("type")
	public JsonSchema jsonSchema;

	public String getJsonSchema() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			return mapper.writeValueAsString(jsonSchema);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setJsonSchema(JsonSchema jsonSchema) {
		this.jsonSchema = jsonSchema;

	}
}
