package com.demoapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class ReportTest extends BaseTest {

	@Test
	public void testReport() throws ParseException {

		Long firstCateoryID = createCategory("First category").getId();
		Long secondCateoryID = createCategory("Second category").getId();

		Long firstProductID = createProduct("First product", 2.00, "sku1", firstCateoryID).getId();
		Long secondProductID = createProduct("Second product", 1.00, "sku2", secondCateoryID).getId();

		Long orderItem1 = createOrderItem(3L, firstProductID, null).getId(); // 6
		Long orderItem2 = createOrderItem(2L, secondProductID, null).getId(); // 2
		Long orderItem3 = createOrderItem(10L, secondProductID, null).getId(); // 10

		List<Long> orderItems1 = List.of(orderItem1); // 6
		List<Long> orderItems2 = List.of(orderItem2, orderItem3); // 12

		createOrder(2L, orderItems1); // 12 = 2*6
		createOrder(3L, orderItems2); // 36 = 3*12

		Map<String, Double> report = callReport();

		assertFalse(report.isEmpty());
		String date = report.keySet().iterator().next();
		SimpleDateFormat sr = new SimpleDateFormat("yyyy-MM-dd");
		sr.parse(date);

		assertEquals(Double.valueOf(48), report.get(date)); // 12 + 36

	}

	private Map<String, Double> callReport() {
		ResponseEntity<Map<String, Double>> response = restTemplate.exchange("/api/report", HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertTrue(response.getStatusCode().is2xxSuccessful());

		return response.getBody();
	}

}
