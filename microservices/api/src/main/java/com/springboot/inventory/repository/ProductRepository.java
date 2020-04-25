package com.springboot.inventory.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.springboot.inventory.model.Product;

@EnableScan
public interface ProductRepository extends CrudRepository<Product, String>{
	
	Product findById(String id);
	
	List<Product> findAll();
	
	List<Product> findByReserved(Boolean reserved);
	
}
