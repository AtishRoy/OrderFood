package com.mindtree.order.management.ordermanagementservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.order.management.ordermanagementservice.dto.OrderDTO;
import com.mindtree.order.management.ordermanagementservice.dto.RequestOrderDTO;

@RestController
@RequestMapping("/order")
@RefreshScope
public interface OrderManagementController {

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> fetchOrder(@PathVariable String id);

	@GetMapping
	public ResponseEntity<List<OrderDTO>> fetchAllOrders(@RequestParam("pageNumber") Integer pageNumber,
			@RequestParam("count") Integer count);

	@PostMapping
	public ResponseEntity<Object> placeOrder(@Valid @RequestBody RequestOrderDTO orderDetails);

	@PutMapping
	public ResponseEntity<Object> updateOrder(@Valid @RequestBody RequestOrderDTO orderDetails);

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> cancelOrder(@PathVariable String id);
}
