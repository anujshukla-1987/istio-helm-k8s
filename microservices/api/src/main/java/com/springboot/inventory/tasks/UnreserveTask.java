package com.springboot.inventory.tasks;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.inventory.model.Product;
import com.springboot.inventory.repository.ProductRepository;

@Component
public class UnreserveTask implements Tasklet{
	
	@Autowired
	private ProductRepository productRepository;	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		List<Product> products = productRepository.findByReserved(true);

		for (Product product : products) {
			if(Instant.ofEpochMilli(product.getUpdateDate().getTime()).until(Instant.now(), ChronoUnit.MINUTES) > 30) {
				product.setReserved(false);
				productRepository.save(product);
			}			
		}
		
		return RepeatStatus.FINISHED;
	}

}
