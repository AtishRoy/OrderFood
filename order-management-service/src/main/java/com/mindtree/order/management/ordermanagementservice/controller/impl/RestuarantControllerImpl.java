package com.mindtree.order.management.ordermanagementservice.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.order.management.ordermanagementservice.config.SwaggerConfig;
import com.mindtree.order.management.ordermanagementservice.controller.RestuarantController;
import com.mindtree.order.management.ordermanagementservice.dto.OrderDTO;
import com.mindtree.order.management.ordermanagementservice.mapper.EntityDTOMapper;
import com.mindtree.order.management.ordermanagementservice.model.Order;
import com.mindtree.order.management.ordermanagementservice.service.OrderManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Import(SwaggerConfig.class)
@RequestMapping("/restuarant")
@Api(value = "Restuarant Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = { "Restuarant" }, description = "Api's for retieving the orders for a restuarant")
@RefreshScope
public class RestuarantControllerImpl implements RestuarantController {

	@Autowired
	private OrderManagementService orderManagementService;
	
	@ApiOperation(value = "Fetch order by restuarant ID", response = OrderDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Order returned successfully"),
			@ApiResponse(code = 400, message = "The requested order is not found") })
	@ApiParam(value = "Restuarant ID", name = "restuarant Id")
	@GetMapping("/{id}")
	public ResponseEntity<List<OrderDTO>> fetchOrdersByRestaurantId(@PathVariable String id,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "count", required = false) Integer count) {
		id = id.trim();
		List<Order> orders = orderManagementService.fetchOrdersByRestaurantId(id, pageNumber, count);
		List<OrderDTO> confirmOrders = new ArrayList<OrderDTO>();
		for (Order order : orders) {
			OrderDTO orderDTO = EntityDTOMapper.mapEntityToOrderDTO(order);
			confirmOrders.add(orderDTO);
		}
		return new ResponseEntity<>(confirmOrders, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Fetch order by restuarant ID for the customer", response = OrderDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Order returned successfully"),
			@ApiResponse(code = 400, message = "The requested order is not found") })
	@ApiParam(value = "Restuarant ID", name = "restuarant Id")
	@GetMapping(params="restuarantId" )
	public ResponseEntity<List<OrderDTO>> fetchOrdersForCustomerByRestaurantId(@RequestParam("restuarantId") String restuarantId) {
		restuarantId = restuarantId.trim();
		List<Order> orders = orderManagementService.fetchOrdersOfCustomerByRestaurantId(restuarantId);
		List<OrderDTO> confirmOrders = new ArrayList<OrderDTO>();
		for (Order order : orders) {
			OrderDTO orderDTO = EntityDTOMapper.mapEntityToOrderDTO(order);
			confirmOrders.add(orderDTO);
		}
		return new ResponseEntity<>(confirmOrders, HttpStatus.OK);
	}
}
