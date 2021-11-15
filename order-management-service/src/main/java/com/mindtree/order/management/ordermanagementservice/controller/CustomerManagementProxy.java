package com.mindtree.order.management.ordermanagementservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mindtree.order.management.ordermanagementservice.controller.impl.CustomerManagementFallback;
import com.mindtree.order.management.ordermanagementservice.dto.CustomerIdResponse;

@FeignClient(name = "customer-management-service", fallback = CustomerManagementFallback.class)
public interface CustomerManagementProxy {

	@GetMapping(value = "/customer/getCustomerId")
	public ResponseEntity<CustomerIdResponse> getCustomerWithId(@RequestHeader("X-ACCESS-TOKEN") String token);

}
