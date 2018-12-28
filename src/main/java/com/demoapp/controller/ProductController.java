package com.demoapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.dto.ProductDTO;
import com.demoapp.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAll() {
		return ResponseEntity.ok(productService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

		return ResponseEntity.ok(productService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO product) {

		return ResponseEntity.ok(productService.save(product));

	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody ProductDTO product) throws ResourceNotFoundException {
		return ResponseEntity.ok(productService.updateProduct(id, product));
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		productService.deleteProduct(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
