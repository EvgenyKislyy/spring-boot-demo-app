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

import com.demoapp.dto.OrderDTO;
import com.demoapp.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public ResponseEntity<List<OrderDTO>> getAll() {
		return ResponseEntity.ok(orderService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		return ResponseEntity.ok().body(orderService.findById(id));
	}

	@PostMapping
	public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO order) {

		return ResponseEntity.ok(orderService.save(order));

	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable(value = "id") Long id, @Valid @RequestBody OrderDTO order)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(orderService.update(id, order));
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		orderService.delete(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
