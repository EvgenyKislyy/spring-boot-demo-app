package com.demoapp.entity.elastic;

import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "order")
public class Order {

	@Id
	private String orderId;

	@Field(type = Nested, includeInParent = true)
	private List<Product> products;

	public Order() {
		super();
	}

	public Order(Long orderId, List<Product> products) {
		super();
		this.orderId = String.valueOf(orderId);
		this.products = products;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}