# spring-rest-commons-options

<p align="center"><img src="/images/Logo.png" width="100" height="80"><p> 

[![Build Status](https://travis-ci.org/damianwajser/spring-rest-commons-options.svg?branch=master)](https://travis-ci.org/damianwajser/spring-rest-commons-options) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/1400/badge)](https://bestpractices.coreinfrastructure.org/projects/1400) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.damianwajser/spring-rest-commons-options/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.damianwajser/spring-rest-commons-options) [![Maintainability](https://api.codeclimate.com/v1/badges/dc020f5455c0b6f31089/maintainability)](https://codeclimate.com/github/damianwajser/spring-rest-commons-options/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/dc020f5455c0b6f31089/test_coverage)](https://codeclimate.com/github/damianwajser/spring-rest-commons-options/test_coverage)


## Overview

This project contains the general-purpose documentation to spring rest api http options.
Project is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

-----
## Roadmap

Consult the proyect for details on the current [spring-rest-commons-options roadmap](https://github.com/damianwajser/spring-rest-commons-options/projects/1).

## Get it!

### Maven

Functionality of this package is contained in Java package `com.github.damianwajser`, and can be used using following Maven dependency:

```xml
<properties>
  ...
  <!-- Use the latest version whenever possible. -->
  <options.spring.docs>0.0.14</options.spring.docs>
  ...
</properties>

<dependencies>
  ...
  <dependency>
    <groupId>com.github.damianwajser</groupId>
    <artifactId>spring-rest-commons-options</artifactId>
    <version>${options.spring.docs}</version>
  </dependency>
  ...
</dependencies>
```

## Usage
```java
@ComponentScan({"com.github.damianwajser","com.test.damianwajser"})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
  
}
```
## License

The Spring Framework is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
