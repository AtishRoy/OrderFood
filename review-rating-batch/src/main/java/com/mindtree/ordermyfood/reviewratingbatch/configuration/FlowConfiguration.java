package com.mindtree.ordermyfood.reviewratingbatch.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step sendEmail() {
		return stepBuilderFactory.get("sendEmail").tasklet((StepContribution arg0, ChunkContext arg1) -> {
			System.out.println("Sent Email...!!");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step initDataBase() {
		return stepBuilderFactory.get("initDataBase").tasklet((StepContribution arg0, ChunkContext arg1) -> {
			System.out.println("Database Init...!!");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Flow flow() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("init");

		flowBuilder.start(sendEmail()).next(initDataBase()).end();

		return flowBuilder.build();
	}

}
