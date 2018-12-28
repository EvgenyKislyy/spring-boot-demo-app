package com.demoapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.demoapp.dao.OrderItemRepository;
import com.demoapp.dao.OrderRepository;
import com.demoapp.dao.ProductRepository;
import com.demoapp.dto.OrderItemDTO;
import com.demoapp.entity.OrderItem;

@Service
public class OrderItemService {
	private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	public List<OrderItemDTO> findAll() {
		List<OrderItemDTO> orderItems = new ArrayList<>();
		for (OrderItem orderItem : orderItemRepository.findAll()) {
			OrderItemDTO map = modelMapper.map(orderItem, OrderItemDTO.class);
			if (orderItem.getProduct() != null) {
				map.setProductId(orderItem.getProduct().getId());
			}
			orderItems.add(map);
		}
		logger.info("Find all, size id {}", orderItems.size());
		return orderItems;
	}

	public OrderItemDTO findById(Long id) throws ResourceNotFoundException {
		logger.info("Find by id {}", id);
		return modelMapper.map(
				orderItemRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Order item not found for this id :: " + id)),
				OrderItemDTO.class);

	}

	public OrderItemDTO save(OrderItemDTO orderItemDTO) {

		OrderItem order = new OrderItem();
		updateOrderItem(orderItemDTO, order);
		logger.info("Save {}", orderItemDTO);
		return modelMapper.map(orderItemRepository.save(order), OrderItemDTO.class);
	}

	private void updateOrderItem(OrderItemDTO orderItemDTO, OrderItem orderItem) {
		boolean productNameChanged = false;
		if (orderItemDTO.getProductId() != null) {
			productNameChanged = true;
			orderItem.setProduct(productRepository.findById(orderItemDTO.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Product item not found for this id :: " + orderItemDTO.getProductId())));
		}
		if (orderItemDTO.getOrderId() != null) {
			orderItem.setOrder(
					orderRepository.findById(orderItemDTO.getOrderId()).orElseThrow(() -> new ResourceNotFoundException(
							"Order item not found for this id :: " + orderItemDTO.getOrderId())));
		}
		orderItem.setQuantity(orderItemDTO.getQuantity());
		if (productNameChanged) {
			Long productID = orderItemDTO.getProductId();
			productService.refreshElasticForProductId(productID);
		}
	}

	public OrderItemDTO updateOrderItem(Long orderItemId, OrderItemDTO orderItemDTO) throws ResourceNotFoundException {
		OrderItem order = orderItemRepository.findById(orderItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Order item not found for this id :: " + orderItemId));

		updateOrderItem(orderItemDTO, order);
		logger.info("Update", orderItemDTO);
		return modelMapper.map(orderItemRepository.save(order), OrderItemDTO.class);

	}

	public Map<String, Boolean> delete(Long id) throws ResourceNotFoundException {
		OrderItem orderItem = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order item not found for this id :: " + id));

		orderItemRepository.deleteById(id);
		if (orderItem.getOrder() != null) {
			Long productID = orderItem.getProduct().getId();
			productService.refreshElasticForProductId(productID);
		}
		logger.info("Delete {}", id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
