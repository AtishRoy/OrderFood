package com.mindtree.order.management.ordermanagementservice.controller.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.mindtree.order.management.ordermanagementservice.config.SwaggerConfig;
import com.mindtree.order.management.ordermanagementservice.controller.OrderManagementController;
import com.mindtree.order.management.ordermanagementservice.dto.ConfirmOrderDTO;
import com.mindtree.order.management.ordermanagementservice.dto.OrderDTO;
import com.mindtree.order.management.ordermanagementservice.dto.RequestOrderDTO;
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
@RequestMapping("/order")
@Api(value = "Order Management Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {
		"Order Management" }, description = "Api's for managing the orders")
@RefreshScope
public class OrderManagementControllerImpl implements OrderManagementController {

	@Autowired
	private OrderManagementService orderManagementService;

	@Autowired
	private EntityLinks entityLinks;

	@ApiOperation(value = "Fetch order by order ID", response = OrderDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Order returned successfully"),
			@ApiResponse(code = 400, message = "The requested order is not found") })
	@ApiParam(value = "Order ID", name = "Order Id")
	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> fetchOrder(@PathVariable String id) {
		id = id.trim();
		long orderId = Long.parseLong(id);
		Order order = orderManagementService.fetchOrder(orderId);
		OrderDTO orderDTO = EntityDTOMapper.mapEntityToOrderDTO(order);
		return new ResponseEntity<>(orderDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Fetch list of orders", response = OrderDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Orders returned successfully"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping
	public ResponseEntity<List<OrderDTO>> fetchAllOrders(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "count", required =  false) Integer count) {
		List<Order> orders = orderManagementService.fetchAllOrders(pageNumber, count);
		List<OrderDTO> confirmOrders = new ArrayList<OrderDTO>();
		for (Order order : orders) {
			OrderDTO orderDTO = EntityDTOMapper.mapEntityToOrderDTO(order);
			confirmOrders.add(orderDTO);
		}
		return new ResponseEntity<>(confirmOrders, HttpStatus.OK);
	}

	@ApiOperation(value = "Place order", response = ConfirmOrderDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Order places successfully"),
			@ApiResponse(code = 400, message = "Input data mismatch") })

	@PostMapping
	public ResponseEntity<Object> placeOrder(@Valid @RequestBody RequestOrderDTO orderDetails) {
		Order order = EntityDTOMapper.mapDTOToEntity(orderDetails);
		order = orderManagementService.placeOrder(order);
		ConfirmOrderDTO confirmOrder = EntityDTOMapper.mapEntityToDTO(order);
		Link getOrderDetail = linkTo(
				methodOn(OrderManagementControllerImpl.class).fetchOrder(confirmOrder.getOrderId().toString()))
						.withRel("get order details");
		Resource<ConfirmOrderDTO> resource = new Resource<>(confirmOrder, getOrderDetail);
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update order", response = ConfirmOrderDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Order updates successfully"),
			@ApiResponse(code = 400, message = "Input data mismatch") })
	@PutMapping
	public ResponseEntity<Object> updateOrder(@Valid @RequestBody RequestOrderDTO orderDetails) {
		Order order = EntityDTOMapper.mapDTOToEntity(orderDetails);
		order = orderManagementService.updateOrder(order);
		ConfirmOrderDTO confirmOrder = EntityDTOMapper.mapEntityToDTO(order);
		Link getOrderDetail = linkTo(
				methodOn(OrderManagementControllerImpl.class).fetchOrder(confirmOrder.getOrderId().toString()))
						.withRel("get order details");
		Resource<ConfirmOrderDTO> resource = new Resource<>(confirmOrder, getOrderDetail);
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Cancel order", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Order cancells successfully"),
			@ApiResponse(code = 400, message = "Input data mismatch"),
			@ApiResponse(code = 404, message = "Order not found") })
	@ApiParam(value = "Order ID", name = "Order Id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> cancelOrder(@PathVariable String id) {
		id = id.trim();
		long orderId = Long.parseLong(id);
		String message = orderManagementService.cancelOrder(orderId);
		Link getOrderDetail = linkTo(
				methodOn(OrderManagementControllerImpl.class).fetchOrder(id))
						.withRel("get cancelled order details");
		Resource<String> resource = new Resource<>(message, getOrderDetail);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
