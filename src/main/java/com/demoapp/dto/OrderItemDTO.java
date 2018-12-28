package com.demoapp.dto;

import javax.validation.constraints.NotNull;

public class OrderItemDTO {

	public Long id;

	@NotNull
	public Long quantity;

	@NotNull
	public Long productId;

	public Long orderId;

	public OrderItemDTO() {
		super();
	}

	public OrderItemDTO(Long quantity, Long productId, Long orderId) {
		super();
		this.quantity = quantity;
		this.productId = productId;
		this.orderId = orderId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "OrderItemDTO [id=" + id + ", quantity=" + quantity + ", productId=" + productId + ", orderId=" + orderId
				+ "]";
	}

}
