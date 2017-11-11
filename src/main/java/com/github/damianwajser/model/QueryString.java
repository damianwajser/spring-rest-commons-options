package com.github.damianwajser.model;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class QueryString {

	private Collection<RequestParams> requestParams = new ArrayList<>();

	public String toString(){
		StringBuilder pathVariable = new StringBuilder();
		requestParams.forEach(r->{
		if(pathVariable.length()>0){
			pathVariable.append("&");
		}
		
		pathVariable.append(r.getName()+"={"+r.getName()+"}");
		});
		return pathVariable.toString();
	}

	public void add(RequestParams requestParam) {
		this.requestParams.add(requestParam);
	}
}
