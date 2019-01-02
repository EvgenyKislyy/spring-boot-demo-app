package com.demoapp.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.demoapp.dao.CategoryRepository;
import com.demoapp.dao.OrderItemRepository;
import com.demoapp.dao.OrderRepository;
import com.demoapp.dao.ProductRepository;
import com.demoapp.dto.CategoryDTO;
import com.demoapp.dto.OrderDTO;
import com.demoapp.dto.OrderItemDTO;
import com.demoapp.dto.ProductDTO;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

	@Autowired
	protected TestRestTemplate restTemplate;

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductRepository productRepository;

	public void cleanTestDB() {
		orderItemRepository.deleteAll();
		orderRepository.deleteAll();
		productRepository.deleteAll();
		categoryRepository.deleteAll();

	}

	protected CategoryDTO createCategory(String name) {
		HttpEntity<?> request = new HttpEntity<Object>(new CategoryDTO(name), null);
		ResponseEntity<CategoryDTO> response = restTemplate.exchange("/categories", HttpMethod.POST, request,
				CategoryDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected List<CategoryDTO> getCategories() {
		ResponseEntity<List<CategoryDTO>> response = restTemplate.exchange("/categories", HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected ProductDTO createProduct(String name, Double price, String sku, long id) {
		HttpEntity<?> request = new HttpEntity<Object>(new ProductDTO(name, price, sku, id), null);
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/products", HttpMethod.POST, request,
				ProductDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected List<ProductDTO> getProducts() {
		ResponseEntity<List<ProductDTO>> response = restTemplate.exchange("/products", HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected OrderItemDTO createOrderItem(Long quantity, Long productId, Long orderId) {
		HttpEntity<?> request = new HttpEntity<Object>(new OrderItemDTO(quantity, productId, null));
		ResponseEntity<OrderItemDTO> response = restTemplate.exchange("/order_items", HttpMethod.POST, request,
				OrderItemDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected OrderDTO createOrder(Long amount, List<Long> orderItems) {
		HttpEntity<?> request = new HttpEntity<Object>(new OrderDTO(null, amount, orderItems), null);
		ResponseEntity<OrderDTO> response = restTemplate.exchange("/orders", HttpMethod.POST, request, OrderDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected OrderItemDTO getOrderItemById(Long id) {
		ResponseEntity<OrderItemDTO> response = restTemplate.exchange("/order_items/" + id, HttpMethod.GET, null,
				OrderItemDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected List<OrderItemDTO> getOrderItems() {
		ResponseEntity<List<OrderItemDTO>> response = restTemplate.exchange("/order_items", HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	protected void deleteOrderItem(Long id) {
		ResponseEntity<String> response = restTemplate.exchange("/order_items/" + id, HttpMethod.DELETE, null,
				String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

}
