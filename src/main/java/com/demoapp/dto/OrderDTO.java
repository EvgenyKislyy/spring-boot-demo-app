package com.demoapp.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class OrderDTO {

	private Long id;

	@NotNull
	private Long amount;

	private List<Long> orderItemId;

	public OrderDTO() {
		super();
	}

	public OrderDTO(Long id, Long amount, List<Long> orderItemsId) {
		super();
		this.id = id;
		this.amount = amount;
		this.orderItemId = orderItemsId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public List<Long> getOrderItems() {
		return orderItemId;
	}

	public void setOrderItems(List<Long> orderItems) {
		this.orderItemId = orderItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "OrderDTO [id=" + id + ", amount=" + amount + ", orderItems=" + orderItemId + "]";
	}

}
