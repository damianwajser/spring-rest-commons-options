package com.github.damianwajser.model;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.damianwajser.model.abstracts.GenericParameter;

public class InputMode extends GenericParameter{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8706741155379757541L;
	@NotEmpty(message="El campo description es obligatorio")
	private String description;
	
	@NotEmpty(message="El campo channelCode es obligatorio")
	private String channelCode;
	
	@NotEmpty(message="El campo inputMode es obligatorio")
	@Length(max=3,message="El campo inputMode es invalido")
	private String inputMode;
	
	
	public String getChannelCode() {
		return channelCode;
	}

	public String getInputMode() {
		return inputMode;
	}

	public void setInputMode(String inputMode) {
		this.inputMode = inputMode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
