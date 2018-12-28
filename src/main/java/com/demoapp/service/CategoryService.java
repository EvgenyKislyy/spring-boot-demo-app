package com.demoapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.demoapp.dao.CategoryRepository;
import com.demoapp.dao.ProductRepository;
import com.demoapp.dto.CategoryDTO;
import com.demoapp.dto.ProductDTO;
import com.demoapp.entity.Category;
import com.demoapp.entity.Product;

@Service
public class CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<CategoryDTO> findAll() {
		List<CategoryDTO> categoriesDTO = new ArrayList<>();
		for (Category category : categoryRepository.findAll()) {
			categoriesDTO.add(modelMapper.map(category, CategoryDTO.class));
		}
		logger.info("Find all, size {}", categoriesDTO.size());
		return categoriesDTO;
	}

	public CategoryDTO findById(Long id) throws ResourceNotFoundException {
		logger.info("Find by id {}", id);
		return modelMapper.map(
				categoryRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + id)),
				CategoryDTO.class);

	}

	public CategoryDTO save(@Valid @RequestBody CategoryDTO categoryDTO) {
		Category category = modelMapper.map(categoryDTO, Category.class);
		logger.info("Save {}", category);
		return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
	}

	public CategoryDTO update(Long categoryId, CategoryDTO categoryDTO) throws ResourceNotFoundException {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + categoryId));
		category.setName(categoryDTO.getName());
		logger.info("Update {}", category);
		return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
	}

	public void deleteCategory(Long categoryId) throws ResourceNotFoundException {
		logger.info("Delete by id {}", categoryId);
		categoryRepository.deleteById(categoryId);

	}

	public List<ProductDTO> findProductsById(Long id) {

		List<ProductDTO> categoriesDTO = new ArrayList<>();
		for (Product category : productRepository.findProductsByCategoryId(id)) {
			categoriesDTO.add(modelMapper.map(category, ProductDTO.class));
		}
		logger.info("Find products by category id {}", id);
		return categoriesDTO;

	}
}
