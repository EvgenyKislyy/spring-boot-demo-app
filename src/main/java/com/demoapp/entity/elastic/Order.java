package com.demoapp.entity.elastic;

import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(indexName = "order_index", type = "order")
public class Order {

	@Id
	@JsonIgnore
	private String id;

	private Long orderId;

	@Field(type = Nested, includeInParent = true)
	private List<Product> products;

	public Order() {
		super();
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}