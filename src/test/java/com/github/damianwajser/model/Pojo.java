package com.github.damianwajser.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//NO CAMBIAR EL ORDEN YA QUE SE USA PARA LOS JUNIT
public class Pojo {

	@JsonIgnore
	private String noLoTengoQueVer;

	private boolean visible;

	@NotBlank(message = "validacion")
	private String notBlank;

	private String testGetJson;

	private String testSetJson;

	@JsonProperty("property")
	private String testPropJson;

	public String getTestPropJson() {
		return testPropJson;
	}

	public void setTestPropJson(String testPropJson) {
		this.testPropJson = testPropJson;
	}

	private Date date;

	public String getNotBlank() {
		return notBlank;
	}

	public void setNotBlank(String notBlank) {
		this.notBlank = notBlank;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean isVisible) {
		this.visible = isVisible;
	}

	public String getNoLoTengoQueVer() {
		return noLoTengoQueVer;
	}

	public void setNoLoTengoQueVer(String noLoTengoQueVer) {
		this.noLoTengoQueVer = noLoTengoQueVer;
	}
	//
	// public Date getDate() {
	// return date;
	// }
	//
	// public void setDate(Date date) {
	// this.date = date;
	// }

	@JsonProperty("get")
	public String getTestGetJson() {
		return testGetJson;
	}

	public void setTestGetJson(String testGetJson) {
		this.testGetJson = testGetJson;
	}

	public String getTestSetJson() {
		return testSetJson;
	}

	@JsonProperty("set")
	public void setTestSetJson(String testSetJson) {
		this.testSetJson = testSetJson;
	}
}
