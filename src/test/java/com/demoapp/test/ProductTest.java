package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.demoapp.dto.ProductDTO;

public class ProductTest extends BaseTest {

	@Test
	public void testProducts() {
		List<ProductDTO> products = getProducts();

		assertTrue(products.isEmpty());

		Long idCategory1 = createCategory("First category").getId();
		Long idCategory2 = createCategory("Second category").getId();

		createProduct("First product", 2.00, "sku1", idCategory1);
		createProduct("Second product", 4.20, "sku2", idCategory2);

		products = getProducts();

		assertEquals(2, products.size());

		assertEquals("First product", products.get(0).getName());
		assertEquals(Double.valueOf(2.00), products.get(0).getPrice());
		assertEquals("sku1", products.get(0).getSku());
		// assertEquals("First category", products.get(0).getCategory().getName());

		assertEquals("Second product", products.get(1).getName());
		assertEquals(Double.valueOf(4.20), products.get(1).getPrice());
		assertEquals("sku2", products.get(1).getSku());
		// assertEquals("Second category", products.get(1).getCategory().getName());

		Long firstId = products.get(0).getId();

		updateProduct(firstId, "1st product", 2.2, "sku-1", idCategory2);

		ProductDTO product = getPRoductById(firstId);
		assertEquals("1st product", product.getName());
		assertEquals(Double.valueOf(2.20), product.getPrice());
		assertEquals("sku-1", product.getSku());
		assertEquals(idCategory2, product.getCategoryId());

		products = getProducts();

		assertEquals(2, products.size());

		deleteProduct(firstId);

		products = getProducts();

		assertEquals(1, products.size());

		assertEquals(2, getCategories().size());
	}

	private void deleteProduct(Long id) {
		ResponseEntity<String> response = restTemplate.exchange("/products/" + id, HttpMethod.DELETE, null,
				String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	private ProductDTO getPRoductById(Long id) {
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/products/" + id, HttpMethod.GET, null,
				ProductDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	private void updateProduct(Long id, String name, Double price, String sku, Long categoryId) {
		HttpEntity<?> request = new HttpEntity<Object>(new ProductDTO("1st product", 2.2, "sku-1", categoryId), null);
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/products/" + id, HttpMethod.PUT, request,
				ProductDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

}
