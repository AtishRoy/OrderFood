package com.mindtree.restaurant.service.restaurantsearchservice.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket restaurantApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.mindtree.restaurant.service.restaurantsearchservice"))
				//.paths(PathSelectors.ant("/*"))
				.build().apiInfo(apiInfo());

	}

	private ApiInfo apiInfo() {
		ApiInfoBuilder builder = new ApiInfoBuilder();
		Contact contact = new Contact("Vinod Kumar", "https://www.mindtree.com/",
				"vinod.kumar3@mindtree.com");
		ApiInfo apiInfo = builder.contact(contact).description("APIs for searching restaurants")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.version("1.0").title("Search Restaurants System API").build();
		return apiInfo;
	}

}
