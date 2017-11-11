package com.github.damianwajser.model;

import java.util.Collection;

public class DetailField {

	private String name;
	private String type;
	private Collection<String> validation;
//	private boolean presentInRequest;
//	private boolean presentInResponse;
	
	public Collection<String> getValidation() {
		return validation;
	}
	public void setValidation(Collection<String> validation) {
		this.validation = validation;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public boolean isPresentInRequest() {
//		return presentInRequest;
//	}
//	public void setPresentInRequest(boolean presentInRequest) {
//		this.presentInRequest = presentInRequest;
//	}
//	public boolean isPresentInResponse() {
//		return presentInResponse;
//	}
//	public void setPresentInResponse(boolean presentInResponse) {
//		this.presentInResponse = presentInResponse;
//	}
}
