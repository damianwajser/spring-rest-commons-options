package com.github.damianwajser.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.damianwajser.utils.ReflectionUtils;

public class RequestHttpMethod {

	private RequestMethod[] httpMethod = new RequestMethod[0];

	public RequestHttpMethod(Method m) {
		ReflectionUtils.filterRequestMappingAnnontations(m).findFirst().ifPresent(
				a -> this.httpMethod = (RequestMethod[]) AnnotationUtils.getValue(getRequestMqpping(a), "method"));
	}

	public RequestMethod[] getHttpMethod() {
		return httpMethod;
	}

	private Annotation getRequestMqpping(Annotation annotation) {
		if (AnnotationUtils.getValue(annotation, "method") == null) {
			annotation = annotation.annotationType().getDeclaredAnnotation(RequestMapping.class);
		}
		return annotation;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
