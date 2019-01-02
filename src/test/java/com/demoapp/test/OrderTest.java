package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
	public void testCreateAndRead() {
		cleanTestDB();
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

		Long order1Id = createOrder(2l, List.of(orderItem1Id, orderItem2Id)).getId();
		Long order2Id = createOrder(3l, List.of(orderItem3Id, orderItem4Id)).getId();

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
	}

	@Test
	public void testUpdate() {
		cleanTestDB();
		Long firstCateoryId = createCategory("First category").getId();
		Long secondCateoryId = createCategory("Second category").getId();

		Long firstProductId = createProduct("First product", 2.00, "sku1", firstCateoryId).getId();
		Long secondProductId = createProduct("Second product", 4.20, "sku2", secondCateoryId).getId();

		Long orderItem1Id = createOrderItem(1l, firstProductId, null).getId();
		Long orderItem2Id = createOrderItem(2l, secondProductId, null).getId();

		Long orderItem3Id = createOrderItem(3l, firstProductId, null).getId();
		Long orderItem4Id = createOrderItem(4l, secondProductId, null).getId();

		Long order1Id = createOrder(2l, List.of(orderItem1Id, orderItem2Id)).getId();
		Long order2Id = createOrder(3l, List.of(orderItem3Id, orderItem4Id)).getId();

		updateOrder(order1Id, 10L, Collections.emptyList());

		OrderDTO order = getOrderById(order1Id);
		assertEquals(Long.valueOf(10l), order.getAmount());

		// remove order item from order via order item crud api, update is only for add
		// new ones
		updateOrder(order1Id, 11L, List.of(orderItem1Id, orderItem2Id, orderItem3Id));
		order = getOrderById(order1Id);
		assertEquals(3, order.getOrderItems().size());
		assertEquals(Long.valueOf(11L), order.getAmount());

		order = getOrderById(order2Id);
		assertEquals(1, order.getOrderItems().size());

	}

	@Test
	public void testDelete() {
		cleanTestDB();
		Long firstCateoryId = createCategory("First category").getId();
		Long secondCateoryId = createCategory("Second category").getId();

		Long firstProductId = createProduct("First product", 2.00, "sku1", firstCateoryId).getId();
		Long secondProductId = createProduct("Second product", 4.20, "sku2", secondCateoryId).getId();

		Long orderItem1Id = createOrderItem(1l, firstProductId, null).getId();
		Long orderItem2Id = createOrderItem(2l, secondProductId, null).getId();

		Long orderItem3Id = createOrderItem(3l, firstProductId, null).getId();
		Long orderItem4Id = createOrderItem(4l, secondProductId, null).getId();

		Long order1Id = createOrder(2l, List.of(orderItem1Id, orderItem2Id)).getId();
		Long order2Id = createOrder(3l, List.of(orderItem3Id, orderItem4Id)).getId();
		List<OrderDTO> orders = getOrders();
		assertEquals(2, orders.size());

		deleteOrder(order1Id);

		orders = getOrders();

		assertEquals(1, orders.size());
		assertEquals(2, orders.get(0).getOrderItems().size());

		assertEquals(2, getOrderItems().size());
		assertEquals(2, getProducts().size());

		deleteOrder(order2Id);
		orders = getOrders();

		assertEquals(0, orders.size());
		assertEquals(0, getOrderItems().size());
		assertEquals(2, getProducts().size());

	}

	@Test
	public void testDeleOrderItemFromOrder() {
		cleanTestDB();
		Long firstCateoryId = createCategory("First category").getId();
		Long secondCateoryId = createCategory("Second category").getId();

		Long firstProductId = createProduct("First product", 2.00, "sku1", firstCateoryId).getId();
		Long secondProductId = createProduct("Second product", 4.20, "sku2", secondCateoryId).getId();

		Long orderItem1Id = createOrderItem(1l, firstProductId, null).getId();
		Long orderItem2Id = createOrderItem(2l, secondProductId, null).getId();

		Long orderItem3Id = createOrderItem(3l, firstProductId, null).getId();
		Long orderItem4Id = createOrderItem(4l, secondProductId, null).getId();

		Long order1Id = createOrder(2l, List.of(orderItem1Id, orderItem2Id)).getId();
		Long order2Id = createOrder(3l, List.of(orderItem3Id, orderItem4Id)).getId();

		deleteOrderItem(orderItem4Id);
		OrderDTO order = getOrderById(order2Id);
		assertEquals(1, order.getOrderItems().size());
		assertEquals(3, getOrderItems().size());
		assertEquals(2, getProducts().size());
	}

	@Test
	public void testEmptyOrder() {
		cleanTestDB();

		Long order1Id = createOrder(2l, null).getId();
		assertNotNull(order1Id);
		OrderDTO order = getOrderById(order1Id);
		assertTrue(order.getOrderItems().isEmpty());
		assertEquals(Long.valueOf(2L), order.getAmount());

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
