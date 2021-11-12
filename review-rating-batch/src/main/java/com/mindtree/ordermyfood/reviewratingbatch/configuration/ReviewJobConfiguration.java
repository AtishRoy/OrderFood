package com.mindtree.ordermyfood.reviewratingbatch.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.mindtree.ordermyfood.reviewratingbatch.model.ReviewandRating;
import com.mindtree.ordermyfood.reviewratingbatch.proxy.RestaurantSearchProxy;

@Configuration
public class ReviewJobConfiguration extends DefaultBatchConfigurer {

	Logger logger = LoggerFactory.getLogger(ReviewJobConfiguration.class);

    private String token = "eyJhdWQiOiJvcmRlcm15Zm9vZHNwcmluZzMwMSIsInN1YiI6IkJpUDJKM1Q2ZVhWWHF4emVjYzhINUJ1R0h5ZjEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwidXNlcl9pZCI6IkJpUDJKM1Q2ZVhWWHF4emVjYzhINUJ1R0h5ZjEiLCJhdXRoX3RpbWUiOjE1MzgxMzIxMDYsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9vcmRlcm15Zm9vZHNwcmluZzMwMSIsIm5hbWUiOiJwYWxsYXZpIHNhdGhlZXNoIiwiZXhwIjoxNTM4MTM5NTg2LCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7Imdvb2dsZS5jb20iOlsiMTE4MzQ2NDMyMzEyOTY5NTgzODEyIl0sImVtYWlsIjpbInBhbGxhdmlzYXRoZWVzaDE1QGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifSwiaWF0IjoxNTM4MTM1OTg2LCJlbWFpbCI6InBhbGxhdmlzYXRoZWVzaDE1QGdtYWlsLmNvbSIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLWhOZVA2VDYtUW5FL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FBTjMxRFhzYkZBQ2pscVdkZDUzY3RBYkFra2VVTmU2SUEvbW8vcGhvdG8uanBnIn0="; 

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobExplorer jobExplorer;

	@Autowired
	public JobLauncher jobLauncher;

	@Autowired
	public JobRepository jobRepository;

	@Autowired
	public JobRegistry jobRegistry;

	@Autowired
	public DataSource dataSource;

	@Autowired
	RestaurantSearchProxy restaurantSearchProxy;

	@Bean
	public JdbcCursorItemReader<ReviewandRating> ReadReviewandRating() {
		JdbcCursorItemReader<ReviewandRating> itemReader = new JdbcCursorItemReader<>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql(
				"SELECT restaurant_id, ROUND(avg(rating),1) as avgrating from  orchard18.review_rating group by restaurant_id order by restaurant_id ");
		itemReader.setRowMapper((rs, rowNum) -> {
			return new ReviewandRating(rs.getString("restaurant_id"), rs.getFloat("avgrating"));
		});
		return itemReader;
	}

	@Override
	public JobLauncher getJobLauncher() {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		launcher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		try {
			launcher.afterPropertiesSet();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return launcher;
	}

	@Bean
	public JobOperator jobOperator() throws Exception {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		jobOperator.setJobExplorer(jobExplorer);
		jobOperator.setJobLauncher(jobLauncher);
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobRepository(jobRepository);
		jobOperator.afterPropertiesSet();
		return jobOperator;
	}

	@Bean
	public ItemWriter<ReviewandRating> writterReviewandRating() {
	    System.out.println("**********"+token);
		return items -> {
			restaurantSearchProxy.updateAverageRating(token, items);
		};
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("reviewandratingjob").incrementer(new RunIdIncrementer())
				.start(reviewandRatingStep()).build();
	}

	@Bean
	public Step reviewandRatingStep() {
		return stepBuilderFactory.get("readreviewandratingstep").<ReviewandRating, ReviewandRating>chunk(10)
				.reader(ReadReviewandRating()).writer(writterReviewandRating()).build();
	}
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		return jobRegistryBeanPostProcessor;
	}

}
