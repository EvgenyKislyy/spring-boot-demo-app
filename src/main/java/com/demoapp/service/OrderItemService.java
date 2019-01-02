package com.demoapp.service;

import java.util.ArrayList;
import java.util.List;

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
	private ModelMapper modelMapper;

	@Autowired
	private ElasticService elasticService;

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

		OrderItem orderItem = new OrderItem();
		updateOrderItem(orderItemDTO, orderItem);
		logger.info("Save {}", orderItemDTO);
		orderItem = orderItemRepository.save(orderItem);
		elasticService.refreshOrder(orderItemDTO.getOrderId());
		return modelMapper.map(orderItem, OrderItemDTO.class);
	}

	public OrderItemDTO update(Long orderItemId, OrderItemDTO orderItemDTO) throws ResourceNotFoundException {
		OrderItem orderItem = orderItemRepository.findById(orderItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Order item not found for this id :: " + orderItemId));
		updateOrderItem(orderItemDTO, orderItem);
		orderItem = orderItemRepository.save(orderItem);
		logger.info("Update", orderItemDTO);
		elasticService.refreshOrder(orderItemDTO.getOrderId());
		if (orderItem.getOrder() != null) {
			elasticService.refreshOrder(orderItem.getOrder().getId());
		}
		return modelMapper.map(orderItem, OrderItemDTO.class);

	}

	private void updateOrderItem(OrderItemDTO orderItemDTO, OrderItem orderItem) {

		if (orderItemDTO.getProductId() != null) {
			orderItem.setProduct(productRepository.findById(orderItemDTO.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Product not found for this id :: " + orderItemDTO.getProductId())));
		}

		if (orderItemDTO.getOrderId() != null) {
			orderItem.setOrder(
					orderRepository.findById(orderItemDTO.getOrderId()).orElseThrow(() -> new ResourceNotFoundException(
							"Order not found for this id :: " + orderItemDTO.getOrderId())));

		}

		orderItem.setQuantity(orderItemDTO.getQuantity());

	}

	public void delete(Long id) throws ResourceNotFoundException {
		OrderItem orderItem = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order item not found for this id :: " + id));

		if (orderItem.getOrder() != null) {
			elasticService.refreshOrder(orderItem.getOrder().getId());
		}

		orderItemRepository.delete(orderItem);
		logger.info("Delete {}", id);

	}

}
