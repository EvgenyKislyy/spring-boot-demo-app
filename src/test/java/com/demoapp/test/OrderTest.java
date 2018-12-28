package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.demoapp.dto.OrderDTO;

public class OrderTest extends BaseTest {

	@Test
	public void testOrders() {
		List<OrderDTO> orders = getOrders();

		assertTrue(orders.isEmpty());

		Long firstCateoryId = createCategory("First category").getId();
		Long secondCateoryId = createCategory("Second category").getId();

		Long firstProductId = createProduct("First product", 2.00, "sku1", firstCateoryId).getId();
		Long secondProductId = createProduct("Second product", 4.20, "sku2", secondCateoryId).getId();

		Long orderItem1Id = createOrderItem(1l, firstProductId, null).getId();
		Long orderItem2Id = createOrderItem(2l, secondProductId, null).getId();

		Long orderItem3Id = createOrderItem(3l, firstProductId, null).getId();
		Long orderItem4Id = createOrderItem(4l, secondProductId, null).getId();

		List<Long> orderItems1 = List.of(orderItem1Id, orderItem2Id);
		List<Long> orderItems2 = List.of(orderItem3Id, orderItem4Id);

		Long order1Id = createOrder(2l, orderItems1).getId();
		Long order2Id = createOrder(3l, orderItems2).getId();

		orders = getOrders();

		assertEquals(2, orders.size());

		assertEquals(Long.valueOf(2l), orders.get(0).getAmount());
		assertEquals(2, orders.get(0).getOrderItems().size());
		assertEquals(orderItem1Id, orders.get(0).getOrderItems().get(0));
		assertEquals(orderItem2Id, orders.get(0).getOrderItems().get(1));
		assertEquals(Long.valueOf(2l), orders.get(0).getAmount());

		assertEquals(Long.valueOf(3l), orders.get(1).getAmount());
		assertEquals(2, orders.get(1).getOrderItems().size());
		assertEquals(orderItem3Id, orders.get(1).getOrderItems().get(0));
		assertEquals(orderItem4Id, orders.get(1).getOrderItems().get(1));
		assertEquals(Long.valueOf(3l), orders.get(1).getAmount());

		updateOrder(order1Id, 10L, Collections.emptyList());

		OrderDTO order = getOrderById(order1Id);
		assertEquals(Long.valueOf(10l), order.getAmount());

		orders = getOrders();

		assertEquals(2, orders.size());

		deleteOrder(order1Id);

		orders = getOrders();

		assertEquals(1, orders.size());
		assertEquals(2, orders.get(0).getOrderItems().size());

		assertEquals(2, getOrderItems().size());
		assertEquals(2, getProducts().size());

		deleteOrderItem(orderItem4Id);
		order = getOrderById(order2Id);
		assertEquals(1, order.getOrderItems().size());

		assertEquals(2, getProducts().size());

	}

	private void deleteOrder(Long id) {
		ResponseEntity<String> response = restTemplate.exchange("/orders/" + id, HttpMethod.DELETE, null, String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	private OrderDTO getOrderById(Long id) {
		ResponseEntity<OrderDTO> response = restTemplate.exchange("/orders/" + id, HttpMethod.GET, null,
				OrderDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

	private void updateOrder(Long id, Long amount, List<Long> orderItems) {
		HttpEntity<?> request = new HttpEntity<Object>(new OrderDTO(null, amount, orderItems), null);
		ResponseEntity<OrderDTO> response = restTemplate.exchange("/orders/" + id, HttpMethod.PUT, request,
				OrderDTO.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());

	}

	private List<OrderDTO> getOrders() {
		ResponseEntity<List<OrderDTO>> response = restTemplate.exchange("/orders", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<OrderDTO>>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}

}
