package com.github.damianwajser.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.damianwajser.model.details.DetailField;

public class CollectionResources {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionResources.class);
	@JsonProperty("resources")
	private Map<String, Resource> resources = new HashMap<>();
	@JsonProperty("httpCodes")
	private Map<Integer, List<DetailField>> httpCodes = new HashMap<>();

	public void addHttpCode(int value, List<DetailField> fields) {
		this.httpCodes.put(value, fields);

	}

	public void addEndpoint(Endpoint e) {
		LOGGER.debug("Añadiendo endpoint: {}", e);
		String[] relatives = e.getUrl().trim().split("/");
		int i = 0;
		String path = "";
		boolean isPathVariable = false;
		while (i < relatives.length && !isPathVariable) {
			String relative = relatives[i++];
			isPathVariable = relative.contains("{");
			if (StringUtils.isNoneEmpty(relative) && !isPathVariable) {
				path = path + "/" + relative;
				if (!path.matches("/public|/private|/private/v1|/public/v1")) {
					Resource resource = this.resources.get(path);
					if (resource == null) {
						resource = new Resource();
					}
					resource.addEndpoint(e);
					this.resources.put(path, resource);
				}
			}

		}
	}

	@JsonIgnore
	public SortedSet<String> getEndpointsList() {
		return new TreeSet<>(this.resources.keySet());
	}

	public CollectionResources filterPath(String path) {
		CollectionResources aux = new CollectionResources();
		Map<String, Resource> resources = new HashMap<>();
		resources.put(path, this.resources.get(path));
		aux.resources = resources;
		aux.httpCodes = this.httpCodes;
		return aux;
	}

	@JsonIgnore
	public Resource getResource(String path) {
		return this.resources.get(path);
	}

	@JsonIgnore
	public Map<String, Resource> getResources() {
		return this.resources;
	}
	@JsonIgnore
	public Map<Integer, List<DetailField>> getHttpCodes() {
		return this.httpCodes;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
