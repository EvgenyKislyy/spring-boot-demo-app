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

import com.demoapp.dto.CategoryDTO;
import com.demoapp.dto.ProductDTO;
import com.demoapp.service.CategoryService;

@RestController
@RequestMapping
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getAll() {
		return ResponseEntity.ok(categoryService.findAll());
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		return ResponseEntity.ok(categoryService.findById(id));
	}

	@GetMapping("/categories/{id}/products")
	public ResponseEntity<List<ProductDTO>> getCategoryProducts(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(categoryService.findProductsById(id));
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO category) {
		return ResponseEntity.ok(categoryService.save(category));
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody CategoryDTO category) throws ResourceNotFoundException {

		return ResponseEntity.ok(categoryService.update(id, category));
	}

	@DeleteMapping("/categories/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		categoryService.deleteCategory(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
