package com.mindtree.order.management.ordermanagementservice.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mindtree.order.management.ordermanagementservice.controller.CustomerManagementProxy;
import com.mindtree.order.management.ordermanagementservice.dto.CustomerIdResponse;

@Component
public class CustomerManagementFallback implements CustomerManagementProxy {

	@Override
	public ResponseEntity<CustomerIdResponse> getCustomerWithId(String token) {
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("1");
		customerResponse.setEmail("atish1.roy@gmail.com");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse, HttpStatus.OK);
		return response;
	}

}
