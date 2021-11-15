package com.mindtree.customer.management.service;

import java.util.List;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.mindtree.customer.management.model.Customer;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerService.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Service
@RefreshScope
public interface CustomerService {

	// @Transactional
	Customer addCustomer(Customer customer);

	Customer getCustomer(String email);

	Customer getCustomerForId(String email);

	List<Customer> getCustomers(String status);

	// @Transactional
	Customer updateCustomer(Customer customer);

	// @Transactional
	Customer deleteCustomer(String email);
}
