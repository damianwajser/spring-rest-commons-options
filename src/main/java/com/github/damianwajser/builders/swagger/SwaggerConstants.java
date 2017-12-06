package com.github.damianwajser.builders.swagger;

public enum SwaggerConstants{
	
	SWAGGERVERSION_PARAM_KEY("swagger"),
	SWAGGERVERSION_PARAM_VALUE("2.0"),
	PATH_PARAM_KEY("basePath"),
	API_HOST_PARAM_KEY("host"),
	API_INFO_PARAM_KEY("info"),
	API_TITLE_PARAM_KEY("title"),
	API_VERSION_PARAM_KEY("version"),
	API_DEFINITION_PARAM_KEY("definitions"),
	SECDEF_PARAM_KEY("securityDefinitions"),

	PATHSVARIABLE_PARAM_KEY("paths"),
	SCHEMES_PARAM_KEY("schemes"),
	RESPONSES_PARAM_KEY("responses"),

	PARAMETERS_PARAM_KEY("parameters"),
	TYPE_PARAM_KEY("type"),
	EXAMPLES_PARAM_KEY("examples"),
	DESCRIPTION_PARAM_KEY("description"),
	PRODUCES_MEDIATYPE_PARAM_KEY("produces"),
	CONSUMES_MEDIATYPE_PARAM_KEY("consumes"),

	NAME_PARAM_KEY("name"),
	ENUM_PARAM_KEY("enum"),
	MAX_PARAM_KEY("maximum"),
	MIN_PARAM_KEY("minimum"),
	MAXLEN_PARAM_KEY("maxLength"),
	MINLEN_PARAM_KEY("minLength"),
	PATTERN_PARAM_KEY("pattern"),
	SCHEMA_PARAM_KEY("schema"),
	DEFAULTVALUE_PARAM_KEY("default"),
	REQUIRED_PARAM_KEY("required"),
	PARAMTYPE_PARAM_KEY("in"),

	PARAMTYPE_PATH("path"),
	PARAMTYPE_HEADER("header"),
	PARAMTYPE_QUERY("query"),
	PARAMTYPE_BODY("body"),

	MIMETYPE_JSON("application/json"),
	MIMETYPE_XML("application/xml"),

	//Securityschemes
	SCOPES_PARAM_KEY("scopes"),
	AUTHURL_PARAM_KEY("authorizationUrl"),
	TOKENURL_PARAM_KEY("tokenUrl"),

	//Flows
	FLOW_PARAM_KEY("flow"),
	CODEFLOW_PARAM_KEY("code"),
	CODEFLOW_VALUE("accessCode"),
	TOKENFLOW_PARAM_KEY("token"),
	TOKENFLOW_VALUE("implicit"),
	OWNERFLOW_PARAM_KEY("owner"),
	OWNERFLOW_VALUE("password"),
	CREDFLOW_PARAM_KEY("credentials"),
	CREDFLOW_VALUE("application"),

	//Parameters
	NAME_MAP_KEY("name"),
	DEFVALUE_MAP_KEY("defValue"),
	DESC_MAP_KEY("desc"),
	TYPE_MAP_KEY("type"),
	ISREQD_MAP_KEY("isReqd"),
	ENUM_MAP_KEY("Enum"),
	SCHEMA_MAP_KEY("schema"),
	MAX_MAP_KEY("max"),
	MIN_MAP_KEY("min"),
	MAXLEN_MAP_KEY("maxLen"),
	MINLEN_MAP_KEY("minLen"),
	EXAMPLE_MAP_KEY("example"),
	PATTERN_MAP_KEY("ptrn"),
	REPEAT_MAP_KEY("repeat"),
	PARAMTYPE_MAP_KEY("paramType"),

	//Typearrays
	ITEMS_PARAM_KEY("items"),
	ARRAYTYPE_PARAM_KEY("array"),
	REFERENCE_PARAM_KEY("$ref"),

	//Securitytypes
	BASICAUTH_RAML("BasicAuthentication"),
	OAUTH2_RAML("OAuth2.0"),
	BASICAUTH_SWGR("basic"),
	OAUTH2_SWGR("oauth2");
	
	private String value;
	
	SwaggerConstants(String value){
		this.value = value;
		
	}
	
	public String getValue() {
		return this.value;
	}
}