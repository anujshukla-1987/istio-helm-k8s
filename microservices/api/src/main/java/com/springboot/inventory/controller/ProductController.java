package com.springboot.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springboot.inventory.model.Product;
import com.springboot.inventory.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Product> findAll() {
		return productRepository.findAll();
	}	

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Product findById(@PathVariable String id) {
		return productRepository.findById(id);
	}
	
	@RequestMapping(path = "/callNodeMs", method = RequestMethod.GET)
	public @ResponseBody String callNodeMs() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://nodejs-backend-service:4000/boot-node";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resourceUrl, String.class);
		return response.getBody();
		
	}
	
	@RequestMapping(path = "/callPyMsFromNode", method = RequestMethod.GET)
	public @ResponseBody String callPyMsFromNode() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://nodejs-backend-service:4000/boot-node-py";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resourceUrl, String.class);
		return response.getBody();
	}
	
	@RequestMapping(path = "/callPyMs", method = RequestMethod.GET)
	public @ResponseBody String callPyMs() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://python-backend-service:5000/boot-py";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resourceUrl, String.class);
		return response.getBody();
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public void deleteById(@PathVariable String id) {
		productRepository.delete(id);
	}

	@RequestMapping(path = "/add", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Product addProduct(@RequestBody Product product) {
		Product newProduct = productRepository.save(product);
		return newProduct;
	}

	@RequestMapping(path = "/update/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<Product> updateCatalogEntry(@PathVariable String id,
			@RequestBody Product product) {
		Product selectedProduct = productRepository.findById(id);

		if (selectedProduct == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		selectedProduct.setReserved(product.getReserved());
		selectedProduct.setPrice(product.getPrice());
		selectedProduct.setProductName(product.getProductName());
		selectedProduct.setLocation(product.getLocation());	
		
		productRepository.save(selectedProduct);

		return new ResponseEntity<>(selectedProduct, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/reserve/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<Product> reserveProduct(@PathVariable String id) {
		Product selectedProduct = productRepository.findById(id);
		
		if (selectedProduct == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if(!selectedProduct.getReserved()) {
			selectedProduct.setReserved(true);
		} else {
			return new ResponseEntity<>(selectedProduct, HttpStatus.ALREADY_REPORTED);
		}
		
		productRepository.save(selectedProduct);
		
		return new ResponseEntity<>(selectedProduct, HttpStatus.OK);
	}
}
