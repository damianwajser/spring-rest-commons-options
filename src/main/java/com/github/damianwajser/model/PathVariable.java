package com.github.damianwajser.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.damianwajser.utils.ReflectionUtils;

public class PathVariable {
	private List<Parameters> params = new ArrayList<>();
	private static Pattern pattern = Pattern.compile("\\{[a-zA-Z0-9]*\\}");

	public PathVariable(Method m, String relativePath) {
		params = ReflectionUtils.getPathVariable(m);
		if (!params.isEmpty()) {
			Matcher matcher = pattern.matcher(relativePath);
			if (matcher.find()) {
				for (int i = 0; i < params.size(); i++) {
					String param = matcher.group(i);
					params.get(i).setName(param.replaceAll("\\{", "").replaceAll("\\}",""));
				}
			}
		}
	}

	public Collection<Parameters> getParams() {
		return params;
	}

	public void setParams(List<Parameters> params) {
		this.params = params;
	}
}
