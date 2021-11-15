package com.mindtree.customer.management.swagger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <pre>
 * <b>Description : </b>
 * SwaggerConfig.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

	@Bean
	public Docket customerApi() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select().apis(RequestHandlerSelectors.basePackage("com.mindtree.customer.management.controller")).build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		ApiInfoBuilder builder = new ApiInfoBuilder();
		Contact contact = new Contact("Atish Roy", "https://www.atish.roy/", "atish.roy@gmail.com");
		return builder.contact(contact).description("APIs for Customer Management").license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0").version("1.0")
				.title("Customer Management API").build();
	}

}
