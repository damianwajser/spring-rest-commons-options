package com.github.damianwajser.model.header;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Header {
	
	public Header(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	private String name;
	private String type;

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
