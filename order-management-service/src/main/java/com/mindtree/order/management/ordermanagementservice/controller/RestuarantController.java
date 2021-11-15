package com.mindtree.order.management.ordermanagementservice.controller;

import java.util.List;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.order.management.ordermanagementservice.dto.OrderDTO;

@RestController
@RequestMapping("/restuarant")
@RefreshScope
public interface RestuarantController {

	@GetMapping("/{id}")
	public ResponseEntity<List<OrderDTO>> fetchOrdersByRestaurantId(@PathVariable String id, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "count", required = false) Integer count);

	@GetMapping(params = "restuarantId")
	public ResponseEntity<List<OrderDTO>> fetchOrdersForCustomerByRestaurantId(@RequestParam("restuarantId") String restuarantId);
}
