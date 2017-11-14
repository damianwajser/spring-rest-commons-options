package com.github.damianwajser.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJackson2HttpMessageConverter());
	}

	final class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
		YamlJackson2HttpMessageConverter() {
			super(new YAMLMapper().enable(Feature.MINIMIZE_QUOTES), MediaType.parseMediaType("application/x-yaml"));
			this.getObjectMapper().configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
			this.getObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			//this.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
		}
	}
}
