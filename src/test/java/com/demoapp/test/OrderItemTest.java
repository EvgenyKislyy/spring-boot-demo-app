package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.demoapp.dto.CategoryDTO;
import com.demoapp.dto.OrderItemDTO;
import com.demoapp.dto.ProductDTO;

public class OrderItemTest extends BaseTest {
	@Test
	public void testOrderItems() {
		List<OrderItemDTO> orderItems = getOrderItems();

		CategoryDTO category1 = createCategory("First category");
		CategoryDTO category2 = createCategory("Second category");

		ProductDTO product1 = createProduct("First product", 2.00, "sku1", category1.getId());
		ProductDTO product2 = createProduct("Second product", 4.20, "sku2", category2.getId());

		Long order1Id = createOrderItem(22L, product1.getId(), null).getId();
		Long order2Id = createOrderItem(3L, product2.getId(), null).getId();

		orderItems = getOrderItems();

		assertEquals(2, orderItems.size());

		assertEquals(Long.valueOf(22L), getOrderItemById(order1Id).getQuantity());
		assertEquals(product1.getId(), getOrderItemById(order1Id).getProductId());

		assertEquals(Long.valueOf(3L), getOrderItemById(order2Id).getQuantity());
		assertNotNull(getOrderItemById(order2Id).getProductId());

		updateOrder(order1Id, 33L, product1.getId(), null);

		OrderItemDTO order = getOrderItemById(order1Id);
		assertEquals(Long.valueOf(33L), order.getQuantity());
		assertEquals(product1.getId(), getOrderItemById(order1Id).getProductId());

		orderItems = getOrderItems();

		assertEquals(2, orderItems.size());

		deleteOrderItem(order1Id);

		orderItems = getOrderItems();

		assertEquals(1, orderItems.size());

		assertEquals(2, getProducts().size());

	}

	private void updateOrder(Long id, Long quantity, Long productId, Long orderId) {
		HttpEntity<?> request = new HttpEntity<Object>(new OrderItemDTO(quantity, productId, orderId), null);
		ResponseEntity<OrderItemDTO> response = restTemplate.exchange("/order_items/" + id, HttpMethod.PUT, request,
				OrderItemDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

}
