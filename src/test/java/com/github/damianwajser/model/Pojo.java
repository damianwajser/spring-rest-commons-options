package com.github.damianwajser.model;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

//NO CAMBIAR EL ORDEN YA QUE SE USA PARA LOS JUNIT
public class Pojo {

	@JsonIgnore
	private String noLoTengoQueVer;

	private boolean visible;

	@NotBlank(message="validacion")
	private String notBlank;

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
}
