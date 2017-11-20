package com.github.damianwajser.model;

import org.hibernate.validator.constraints.NotBlank;

public class Pojo {
	
	@NotBlank
	private String notBlank;

	public String getNotBlank() {
		return notBlank;
	}

	public void setNotBlank(String notBlank) {
		this.notBlank = notBlank;
	}

	
}
