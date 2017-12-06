package com.github.damianwajser.model.header;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Header {

	private String name;
	private String type;

	public Header() {
	}

	public Header(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
