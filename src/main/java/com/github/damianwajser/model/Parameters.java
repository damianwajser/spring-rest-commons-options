package com.github.damianwajser.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Parameters {
	private boolean required;
	private String name;
	private String type;

	public Parameters(boolean required, String name, String type) {
		super();
		this.type = type;
		this.required = required;
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
