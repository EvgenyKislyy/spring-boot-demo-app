package com.demoapp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductDTO {

	private Long id;

	@NotNull
	private Double price;

	@NotEmpty
	private String sku;

	@NotEmpty
	private String name;

	@NotNull
	private Long categoryId;

	public ProductDTO() {
		super();
	}

	public ProductDTO(String name, Double price, String sku, Long categoryId) {
		super();
		this.price = price;
		this.sku = sku;
		this.name = name;
		this.categoryId = categoryId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", price=" + price + ", sku=" + sku + ", name=" + name + ", categoryId="
				+ categoryId + "]";
	}

}
