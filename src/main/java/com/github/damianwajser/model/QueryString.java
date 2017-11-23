package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.github.damianwajser.utils.ReflectionUtils;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class QueryString {

	private List<Parameters> params = new ArrayList<>();

	public QueryString(Method m) {
		this.params = ReflectionUtils.getQueryString(m);
	}

	public String toString() {
		StringBuilder pathVariable = new StringBuilder();
		params.forEach(r -> {
			if (pathVariable.length() > 0) {
				pathVariable.append("&");
			}

			pathVariable.append(r.getName() + "={" + r.getType() + "}");
		});
		return pathVariable.toString();
	}

	public List<Parameters> getParams() {
		return this.params;
	}
}
