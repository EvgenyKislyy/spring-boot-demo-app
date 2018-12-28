package com.demoapp.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CategoryDTO {

	private Long id;

	@NotEmpty
	public String name;

	private List<Long> productsIds;

	public CategoryDTO() {
		super();
	}

	public CategoryDTO(String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getProductsIds() {
		return productsIds;
	}

	public void setProductsIds(List<Long> productsIds) {
		this.productsIds = productsIds;
	}

	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + ", productsIds=" + productsIds + "]";
	}

}
