package com.springboot.inventory.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springboot.inventory.tasks.UnreserveTask;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;
	
	@Autowired
	UnreserveTask unreserveTask;

	@Bean
	public Step unreserve() {
		return steps.get("unreserve").tasklet(unreserveTask).build();
	}

	@Bean	
	public Job unreserveJob() {
		return jobs.get("reserveJob").start(unreserve()).build();
	}

}
