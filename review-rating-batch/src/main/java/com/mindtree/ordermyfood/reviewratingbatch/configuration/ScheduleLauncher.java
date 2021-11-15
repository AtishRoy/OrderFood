package com.mindtree.ordermyfood.reviewratingbatch.configuration;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleLauncher {

	@Autowired
	private JobOperator jobOperator;

	@Scheduled(cron = "0 * * * * ?")
	public void runJob() throws NoSuchJobException, JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException, JobInstanceAlreadyCompleteException,
			UnexpectedJobExecutionException, JobParametersInvalidException, InterruptedException {
		this.jobOperator.startNextInstance("reviewandratingjob");

	}

}
