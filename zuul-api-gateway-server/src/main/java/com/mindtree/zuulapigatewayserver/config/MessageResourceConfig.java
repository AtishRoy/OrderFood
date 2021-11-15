package com.mindtree.zuulapigatewayserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
@ConfigurationProperties(prefix = "zull.proxy")
public class MessageResourceConfig {

	@Value("${zull.proxy.authentication.required}")
	public String authenticationRequired;


	@Value("${zull.proxy.token.expired}")
	public String tokenExpired;

	@Value("${zull.proxy.invalid.token}")
	public String invalidToken;

}
