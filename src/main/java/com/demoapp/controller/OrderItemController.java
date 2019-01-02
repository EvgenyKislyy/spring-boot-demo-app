package com.demoapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.dto.OrderItemDTO;
import com.demoapp.service.OrderItemService;

@RestController
@RequestMapping("/order_items")
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping
	public ResponseEntity<List<OrderItemDTO>> getAll() {
		return ResponseEntity.ok(orderItemService.findAll());

	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderItemDTO> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		return ResponseEntity.ok(orderItemService.findById(id));
	}

	@PostMapping
	public ResponseEntity<OrderItemDTO> create(@Valid @RequestBody OrderItemDTO orderItem) {
		return ResponseEntity.ok(orderItemService.save(orderItem));
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderItemDTO> update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody OrderItemDTO orderItem) throws ResourceNotFoundException {

		return ResponseEntity.ok(orderItemService.update(id, orderItem));
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		orderItemService.delete(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
