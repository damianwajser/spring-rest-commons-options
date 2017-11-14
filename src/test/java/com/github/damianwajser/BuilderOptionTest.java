package com.github.damianwajser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.github.damianwajser"})
@SpringBootApplication
public class BuilderOptionTest {

	public static void main(String[] args) {
		SpringApplication.run(BuilderOptionTest.class, args);
	}

}
