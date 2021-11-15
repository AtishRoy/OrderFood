package com.mindtree.customer.management.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mindtree.customer.management.dto.CustomerIdResponse;
import com.mindtree.customer.management.dto.CustomerRequest;
import com.mindtree.customer.management.dto.CustomerResponse;
import com.mindtree.customer.management.mapper.CustomerMapper;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.model.OAuthUser;
import com.mindtree.customer.management.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerController.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@RefreshScope
@RestController
@RequestMapping("/customer")
@Api(value = "Customer Management", produces = MediaType.APPLICATION_JSON_VALUE, tags = { "Customer Management" }, description = "Api's for managing the Customer")
public class CustomerController {

	/**
	 * customerService.
	 */
	@Autowired
	private CustomerService customerService;

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	private static final String X_USER_INFO = "X_USER_INFO";

	private static final String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";

	private Gson gson = new Gson();

	@Autowired
	private HttpSession httpSession;

	@Value("${customer.management.customer.added.successfully}")
	private String customerAddedSuccessfully;

	@Value("${customer.management.customer.retrieved.successfully}")
	private String customerRetrievedSuccessfully;

	@Value("${customer.management.customer.updated.successfully}")
	private String customerUpdatedSuccessfully;

	@Value("${customer.management.customer.deleted.successfully}")
	private String customerDeletedSuccessfully;

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Add Customer for a specific Email ID.
	 *
	 * &#64;param customer
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to add a customer given by Customer details", response = CustomerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Customer added successfully"), @ApiResponse(code = 401, message = "You are not authorized to add customer details"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Customer you are trying to add is already present") })
	@PostMapping
	public ResponseEntity<CustomerResponse> addCustomer(@ApiParam(value = "Customer Info", required = true) @Valid @RequestBody CustomerRequest customer) {
		String methodName = "addCustomer";
		String email = getEmailFromToken();
		Customer customerVO = CustomerMapper.buildCustomerForNewCustomer(email, customer);
		Customer savedCustomer = customerService.addCustomer(customerVO);
		CustomerResponse customerResponse = CustomerMapper.buildCustomerResponse(savedCustomer, customerAddedSuccessfully);
		log.info(methodName, gson.toJson(customerResponse));
		return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Get Customer for a specific Email ID.
	 *
	 * &#64;param email
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to get a customer given by Customer Email", response = CustomerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Customer retrived successfully"), @ApiResponse(code = 401, message = "You are not authorized to retrived customer details"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Customer you are trying to retrived is not available") })
	@GetMapping
	public ResponseEntity<CustomerResponse> getCustomer() {
		String methodName = "getCustomer";
		String email = getEmailFromToken();
		Customer foundCustomer = customerService.getCustomer(email);
		CustomerResponse customerResponse = CustomerMapper.buildCustomerResponse(foundCustomer, customerRetrievedSuccessfully);
		log.info(methodName, gson.toJson(customerResponse));
		return new ResponseEntity<>(customerResponse, HttpStatus.OK);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Update Customer for a specific Email ID.
	 *
	 * &#64;param email
	 * &#64;param customer
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to update a customer given by Customer details", response = CustomerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Customer retrived successfully"), @ApiResponse(code = 401, message = "You are not authorized to update customer details"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Customer you are trying to update is not available") })
	@PutMapping
	public ResponseEntity<CustomerResponse> updateCustomer(@ApiParam(value = "Customer Info", required = true) @Valid @RequestBody CustomerRequest customer) {
		String methodName = "updateCustomer";
		String email = getEmailFromToken();
		Customer customerTobeUpdated = CustomerMapper.buildCustomerForUpdateCustomer(email, customer);
		Customer updatedCustomer = customerService.updateCustomer(customerTobeUpdated);
		CustomerResponse customerResponse = CustomerMapper.buildCustomerResponse(updatedCustomer, customerUpdatedSuccessfully);
		log.info(methodName, gson.toJson(customerResponse));
		return new ResponseEntity<>(customerResponse, HttpStatus.OK);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Delete Customer for a specific Email ID.
	 *
	 * &#64;param email
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to delete a customer given by Customer Email", response = CustomerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Customer retrived successfully"), @ApiResponse(code = 401, message = "You are not authorized to delete customer details"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Customer you are trying to delete is not available") })
	@DeleteMapping
	public ResponseEntity<CustomerResponse> deleteCustomer() {
		String methodName = "deleteCustomer";
		String email = getEmailFromToken();
		Customer deleteCustomer = customerService.deleteCustomer(email);
		CustomerResponse customerResponse = CustomerMapper.buildCustomerResponse(deleteCustomer, customerDeletedSuccessfully);
		log.info(methodName, gson.toJson(customerResponse));
		return new ResponseEntity<>(customerResponse, HttpStatus.OK);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Get Customers for a specific status.
	 *
	 * &#64;param status
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to get list of customers by Customer Status", response = CustomerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Customer list retrived successfully"), @ApiResponse(code = 401, message = "You are not authorized to retrived customer list details"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Customer you are trying to retrived is not available") })
	@GetMapping("/getByStatus/{status}")
	public ResponseEntity<List<CustomerResponse>> getCustomers(@Valid @PathVariable("status") String status) {
		String methodName = "getCustomers";
		CustomerMapper.validateStatus(status);
		List<Customer> customer = customerService.getCustomers(status);
		List<CustomerResponse> customerResponseList = CustomerMapper.buildCustomerResponseList(customer);
		log.info(methodName, gson.toJson(customerResponseList));
		return new ResponseEntity<>(customerResponseList, HttpStatus.OK);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * Get Customer ID for a specific Email ID.
	 *
	 * &#64;param email
	 * &#64;return
	 * </pre>
	 */
	@ApiOperation(value = "Api to get customerID given by Customer Email", hidden = true)
	/*
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Customer retrived successfully"),
	 * 
	 * @ApiResponse(code = 401, message = "You are not authorized to retrived customer details"),
	 * 
	 * @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	 * 
	 * @ApiResponse(code = 404, message = "Customer you are trying to retrived is not available") })
	 */
	@GetMapping(value = "/getCustomerId", headers = X_ACCESS_TOKEN)
	public ResponseEntity<CustomerIdResponse> getCustomerWithId(@RequestHeader(X_ACCESS_TOKEN) String oauthToken) {
		String methodName = "getCustomerWithId";
		String email = getEmailFromToken();
		Customer foundCustomer = customerService.getCustomerForId(email);
		CustomerIdResponse customerResponse = CustomerMapper.mapCustomerToCustomerIdResponse(foundCustomer);
		log.info(methodName, gson.toJson(customerResponse));
		return new ResponseEntity<>(customerResponse, HttpStatus.OK);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * getEmailFromToken.
	 *
	 * &#64;return
	 * </pre>
	 */
	private String getEmailFromToken() {
		return CustomerMapper.getEmailFromToken((OAuthUser) httpSession.getAttribute(X_USER_INFO));
	}
}