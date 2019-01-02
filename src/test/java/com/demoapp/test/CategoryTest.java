package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.demoapp.dto.CategoryDTO;
import com.demoapp.dto.ProductDTO;

public class CategoryTest extends BaseTest {

	@Test
	public void testCreateAndRead() {
		cleanTestDB();
		List<CategoryDTO> categories = getCategories();

		assertEquals(0, categories.size());

		Long firstId = createCategory("First category").getId();
		Long secondId = createCategory("Second category").getId();
		Long thirdId = createCategory("Third category").getId();

		categories = getCategories();

		assertEquals(3, categories.size());

		assertEquals("First category", categories.get(0).getName());
		assertEquals("Second category", categories.get(1).getName());
		assertEquals("Third category", categories.get(2).getName());
	}

	@Test
	public void testUpdate() {
		cleanTestDB();
		Long firstId = createCategory("First category").getId();
		Long secondId = createCategory("Second category").getId();
		Long thirdId = createCategory("Third category").getId();

		updateCategory(firstId);

		CategoryDTO category = getCategoryById(firstId);
		assertEquals("1st category", category.getName());

	}

	@Test
	public void testDelete() {
		cleanTestDB();

		Long firstId = createCategory("First category").getId();
		Long secondId = createCategory("Second category").getId();
		Long thirdId = createCategory("Third category").getId();
		List<CategoryDTO> categories = getCategories();
		assertEquals(3, categories.size());
		deleteCategory(firstId);

		categories = getCategories();
		assertEquals(2, categories.size());

	}

	@Test
	public void testProductByCategory() {
		cleanTestDB();
		Long firstId = createCategory("First category").getId();
		Long secondId = createCategory("Second category").getId();
		Long thirdId = createCategory("Third category").getId();

		assertEquals(0, getProductsByCategoryId(secondId).size());

		createProduct("First product", 1.00, "sku1", secondId);
		createProduct("Second product", 2.00, "sku1", secondId);
		createProduct("Third product", 3.00, "sku1", thirdId);

		assertEquals(2, getProductsByCategoryId(secondId).size());
	}

	private void deleteCategory(Long id) {
		ResponseEntity<CategoryDTO> response = restTemplate.exchange("/categories/" + id, HttpMethod.DELETE, null,
				CategoryDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	private CategoryDTO getCategoryById(Long id) {
		ResponseEntity<CategoryDTO> response = restTemplate.exchange("/categories/" + id, HttpMethod.GET, null,
				CategoryDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	private void updateCategory(Long id) {
		HttpEntity<?> request = new HttpEntity<Object>(new CategoryDTO("1st category"), null);
		ResponseEntity<CategoryDTO> response = restTemplate.exchange("/categories/" + id, HttpMethod.PUT, request,
				CategoryDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	protected List<ProductDTO> getProductsByCategoryId(Long id) {
		ResponseEntity<List<ProductDTO>> response = restTemplate.exchange("/categories/" + id + "/products",
				HttpMethod.GET, null, new ParameterizedTypeReference<>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

}
