package com.demoapp.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.demoapp.dao.OrderRepository;
import com.demoapp.dao.es.EsOrderRepository;
import com.demoapp.entity.elastic.Order;
import com.demoapp.entity.elastic.Product;

@Service
public class ElasticService {

	private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);

	@Autowired
	private EsOrderRepository eOrderRepository;

	@Autowired
	private OrderRepository orderRepository;

	public void insert(Long id, List<String> names) {
		try {
			List<Product> products = names.stream().map(n -> new Product(n)).collect(Collectors.toList());
			eOrderRepository.save(new Order(id, products));
			logger.info("Names {} were inserted into order {}", names, id);
		} catch (NoNodeAvailableException e) {
			elasticNotAvailable();
		}
	}

	public List<Order> search(String name) {
		try {
			List<Order> orders = eOrderRepository.findByProductsName(name);
			return orders;
		} catch (NoNodeAvailableException e) {
			elasticNotAvailable();
			return Collections.emptyList();
		}
	}

	public void delete(Long id) {
		try {
			eOrderRepository.removeByOrderId(id);
			logger.info("The order {} was removed", id);
		} catch (NoNodeAvailableException e) {
			elasticNotAvailable();

		}
	}

	public void refreshOrderState(List<Long> ids) {
		logger.error("Refreshing elastic for orders: {}", ids);
		try {
			for (Long orderId : ids) {
				delete(orderId);
				List<String> names = orderRepository.getOrderProductNames(orderId);
				insert(orderId, names);
			}
		} catch (NoNodeAvailableException e) {
			elasticNotAvailable();

		}
	}

	private void elasticNotAvailable() {
		logger.error("Elastic not available");
	}

	public void refresh() {
		eOrderRepository.deleteAll();
		refreshOrderState(orderRepository.getAllOrderIds());
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		refresh();
		logger.info("Elastic was refreshed");
	}

}
