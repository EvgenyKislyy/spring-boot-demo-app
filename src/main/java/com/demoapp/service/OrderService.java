package com.demoapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.demoapp.dao.OrderItemRepository;
import com.demoapp.dao.OrderRepository;
import com.demoapp.dto.OrderDTO;
import com.demoapp.entity.Order;
import com.demoapp.entity.OrderItem;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ElasticService elasticService;

	@Autowired
	private ModelMapper modelMapper;

	public OrderDTO findById(Long id) throws ResourceNotFoundException {
		logger.info("Find by id {}", id);
		return map(orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id)));

	}

	public List<OrderDTO> findAll() {

		List<OrderDTO> orders = new ArrayList<>();
		for (Order order : orderRepository.findAll()) {
			orders.add(map(order));
		}
		logger.info("Find all, size {}", orders.size());
		return orders;

	}

	public OrderDTO save(OrderDTO orderDTO) {
		Order order = modelMapper.map(orderDTO, Order.class);
		order = orderRepository.save(order);
		order = saveOrderItems(orderDTO.getOrderItems(), order);
		logger.info("Save {}", order);

		List<String> productNames = orderRepository.getOrderProductNames(order.getId());
		elasticService.insert(order.getId(), productNames);
		return map(order);

	}

	public OrderDTO update(Long id, OrderDTO orderDTO) throws ResourceNotFoundException {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
		order.setAmount(orderDTO.getAmount());

		// we remove order item ids only via order item id
		if (orderDTO.getOrderItems() != null) {
			order = saveOrderItems(orderDTO.getOrderItems(), order);
		}
		logger.info("Update {}", order);

		List<String> productNames = orderRepository.getOrderProductNames(order.getId());
		elasticService.insert(order.getId(), productNames);
		return map(order);

	}

	private Order saveOrderItems(List<Long> orderItemsIds, Order order) {
		List<OrderItem> orderItems = orderItemsIds.stream()
				.map(orderItemId -> orderItemRepository.findById(orderItemId).orElseThrow(
						() -> new ResourceNotFoundException("Order item not found for this id :: " + orderItemId)))
				.collect(Collectors.toList());

		orderItems.stream().forEach(item -> item.setOrder(order));

		order.setOrderItems(orderItems);
		return orderRepository.save(order);
	}

	public void delete(Long id) throws ResourceNotFoundException {
		orderRepository.deleteById(id);
		logger.info("Delete by id {}", id);
		elasticService.delete(id);

	}

	private OrderDTO map(Order order) {
		List<Long> orderItemIDs = order.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList());
		return new OrderDTO(order.getId(), order.getAmount(), orderItemIDs);
	}

}
