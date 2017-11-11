package com.github.damianwajser.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.damianwajser.model.abstracts.GenericParameter;

public class EndpointsEdp extends GenericParameter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="El campo messageType es obligatorio")
	@Pattern(regexp="(200)|(400)", message="Valor invalido de messageType")
	private String messageType;

	@NotEmpty(message="El campo processingCode es obligatorio")
	@Pattern(regexp="(000000)|(020000)|(200000)|(220000)", message="Valor invalido de processingCode")
	private String processingCode;

	@NotNull(message="El campo transactionType es obligatorio")
	private Integer transactionType;

	@NotEmpty(message="El campo msName es obligatorio")
	private String msName;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public String getMsName() {
		return msName;
	}

	public void setMsName(String msName) {
		this.msName = msName;
	}
	
}
