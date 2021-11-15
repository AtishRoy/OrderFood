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
 * @version $Revision: 1 $ $Date: 2018-09-23 09:36:35 AM $
 * @author $Author: nithya.pranesh $
 * </pre>
 */
@Service
@RefreshScope
public interface CustomerService {

    //@Transactional
    Customer addCustomer(Customer customer);

    Customer getCustomer(String email);

    Customer getCustomerForId(String email);

    List<Customer> getCustomers(String status);

    //@Transactional
    Customer updateCustomer(Customer customer);

    //@Transactional
    Customer deleteCustomer(String email);
}
