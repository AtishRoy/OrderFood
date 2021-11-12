package com.mindtree.ordermyfood.reviewratingbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@EnableFeignClients("com.mindtree.ordermyfood.reviewratingbatch")
@EnableEurekaClient
public class ReviewRatingBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewRatingBatchApplication.class, args);
	}
}
