package com.github.damianwajser.builders.raml.model;

public class QueryParameterRaml {
	private String displayName;
	private String type;
	private String description;
	private Object example;
	private boolean required;

	public QueryParameterRaml(String displayName, String type, String description, boolean required) {
		super();
		this.displayName = displayName;
		this.type = type;
		this.description = description;
		this.required = required;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
