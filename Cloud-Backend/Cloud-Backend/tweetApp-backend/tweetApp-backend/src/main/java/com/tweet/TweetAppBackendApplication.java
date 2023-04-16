package com.tweet;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication

public class TweetAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetAppBackendApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.tweet")).build().apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo("Tweet APP ",
				"An attempt to make an clone of Twitter!",
				"1.0", "Free to use", new springfox.documentation.service.Contact("", "",
						""),
				"HomeProject", "https://github.com", Collections.emptyList());
	}

}
