package com.mindtree.zuulapigatewayserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.context.event.EventListener;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mindtree.zuulapigatewayserver.config.MessageResourceConfig;
import com.netflix.zuul.exception.ZuulException;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulApiGatewayServerApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(ZuulApiGatewayServerApplication.class);

	@Autowired
	MessageResourceConfig resourceConfig;

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ZuulApiGatewayServerApplication.class);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initFireBaseApp() throws ZuulException {
		log.debug("Initialize fire base app!");
		try {

			URL resource = getClass().getResource("/serviceAccountKey.json");
			String file = resource.getFile();
			FileInputStream serviceAccount = new FileInputStream(file);

			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl("https://ordermyfoodapp.firebaseio.com/").build();

			// Initialize fire base application
			if (FirebaseApp.getApps().isEmpty()) { // <--- check with this line
				FirebaseApp.initializeApp(options);
			}
			// FirebaseApp.initializeApp(options);
			log.debug("Initialized fire base app!");
		} catch (IOException exp) {
			ZuulException zuulException = new ZuulException(exp, HttpStatus.SC_UNAUTHORIZED, resourceConfig.invalidToken);
			log.error(String.format("Failed to initialize fire base app with the error ", exp.getMessage()));
			throw new ZuulRuntimeException(zuulException);
		}
	}
}
