package com.springboot.inventory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;

import com.springboot.inventory.model.Product;

@SpringBootApplication 
@EnableScheduling
@ComponentScan(basePackages = { "com.springboot.inventory" })
public class InventoryApplication {
	
	@Autowired
    JobLauncher jobLauncher;
     
    @Autowired
    Job job;
    
    
     
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    
	//@Autowired
	//ProductRepository productRepository;
    
    @PostConstruct
	public void init() {
		
		createProductCatalogTable();
		/*
		 * Product product1 = new Product("Item1", "Location1", false, new
		 * BigDecimal("123.12")); productRepository.save(product1);
		 * 
		 * Product product2 = new Product("Item2", "Location2", false, new
		 * BigDecimal("124132.12")); productRepository.save(product2);
		 */
	}

	public void createProductCatalogTable(){
        try {
            dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Product.class);

            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

            amazonDynamoDB.createTable(tableRequest);
        } catch (ResourceInUseException e) {
            
        }
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	} 
	
	@Scheduled(cron = "0 */1 * * * ?")
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }

}
