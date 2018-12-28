package com.demoapp.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.demoapp.dao.CategoryRepository;
import com.demoapp.dao.ProductRepository;
import com.demoapp.dto.ProductDTO;
import com.demoapp.entity.Product;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ElasticService elasticService;

	@Autowired
	private ModelMapper modelMapper;

	public List<ProductDTO> findAll() {

		List<ProductDTO> productsDTO = new ArrayList<>();
		for (Product category : productRepository.findAll()) {
			productsDTO.add(modelMapper.map(category, ProductDTO.class));
		}
		logger.info("Find all, size {}", productsDTO.size());
		return productsDTO;

	}

	public ProductDTO findById(Long id) throws ResourceNotFoundException {
		logger.info("Find by id {}", id);
		return modelMapper.map(
				productRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id)),
				ProductDTO.class);

	}

	public ProductDTO save(ProductDTO productDTO) {
		Product product = modelMapper.map(productDTO, Product.class);
		if (productDTO.getCategoryId() != null) {
			product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Category not found for this id :: " + productDTO.getCategoryId())));
		}
		logger.info("Save {}", product);
		return modelMapper.map(productRepository.save(product), ProductDTO.class);
	}

	public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws ResourceNotFoundException {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
		boolean nameChanged = false;
		if (!product.getName().equals(productDTO.getName())) {
			nameChanged = true;

		}
		product.setName(productDTO.getName());
		product.setSku(productDTO.getSku());
		product.setPrice(productDTO.getPrice());
		if (productDTO.getCategoryId() != null) {
			product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Category not found for this id :: " + productDTO.getCategoryId())));
		}

		product = productRepository.save(product);
		if (nameChanged) {
			refreshElasticForProductId(id);
		}
		logger.info("Update {}", product);
		return modelMapper.map(product, ProductDTO.class);

	}

	public void deleteProduct(Long id) throws ResourceNotFoundException {
		logger.info("Delete by id {}", id);
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
		productRepository.delete(product);
		refreshElasticForProductId(id);

	}

	public void refreshElasticForProductId(Long id) {
		List<Long> ids = productRepository.getOrderIds(id);
		logger.info("Refresh elastic by product id {}", id);
		elasticService.refreshOrderState(ids);
	}

}
