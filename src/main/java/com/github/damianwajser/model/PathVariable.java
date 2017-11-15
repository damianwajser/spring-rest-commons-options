package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import com.github.damianwajser.utils.ReflectionUtils;

public class PathVariable {
	private Collection<Parameters> params = new ArrayList<>();

	public PathVariable(Method m) {
		params = ReflectionUtils.getPathVariable(m);
	}

	public Collection<Parameters> getParams() {
		return params;
	}

	public void setParams(Collection<Parameters> params) {
		this.params = params;
	}
}
