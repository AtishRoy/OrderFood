/*
 * This code contains copyright information which is the proprietary property
 * of SITA Information Network Computing Limited (SITA). No part of this
 * code may be reproduced, stored or transmitted in any form without the prior
 * written permission of SITA.
 *
 * Copyright (C) SITA Information Network Computing Limited 2010-2011.
 * All rights reserved.
 */
package com.mindtree.customer.management.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindtree.customer.management.exception.CustomerAlreadyExistsException;
import com.mindtree.customer.management.exception.CustomerNotFoundException;
import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.repository.CustomerRepository;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerServiceImplTest.
 *
 * @version $Revision: 1 $ $Date: 2018-09-24 10:50:55 PM $
 * @author $Author: nithya.pranesh $
 * </pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplCacheTest {

    @Autowired
    private CustomerServiceImpl service;

    @MockBean
    private CustomerRepository repository;


    @Before
    public void setUp() {
        service.setHazelcastCacheSwitch("TRUE");
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email1", "124324352", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        Customer addCustomer = service.addCustomer(customer);
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, addCustomer);
    }

    @Test
    public void testAddCustomer1() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
            null, "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        service.emailMap().put("email", customer);
        Customer addCustomer = service.addCustomer(customer);
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        customer.setStatus("ACTIVE");
        Assert.assertEquals(customer, addCustomer);
    }

    @Test(expected=CustomerAlreadyExistsException.class)
    public void testAddCustomerPhone() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        service.addCustomer(customer);
    }

    @Test
    public void testAddCustomerRecreate() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email234", "124324353453", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        Customer addCustomer = service.addCustomer(customer);
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        customer.setStatus("ACTIVE");
        Assert.assertEquals(customer, addCustomer);
    }

    @Test(expected=CustomerAlreadyExistsException.class)
    public void testAddCustomerNegativeFlow() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        service.addCustomer(customer);
    }

    @Test
    public void testGetCustomers() {
        List<Customer> custList = new ArrayList<>();
        Mockito.when(repository.findByStatus(ArgumentMatchers.anyString())).thenReturn(custList);
        String status = "ACTIVE";
        List<Customer> foundCustomer = service.getCustomers(status);
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(0)).findByEmail(customerArgument.capture());
        Assert.assertEquals(custList, foundCustomer);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomersException() {
        Mockito.when(repository.findByStatus(ArgumentMatchers.anyString())).thenReturn(null);
        String status = "ACTIVE";
        service.getCustomers(status);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user111@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        String email = "user111@user.com";
        service.emailMap().put("user111@user.com", customer);
        Customer foundCustomer = service.getCustomer(email);
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(0)).findByEmail(customerArgument.capture());
        Assert.assertEquals(customer, foundCustomer);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomerNotFound() {
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        String email = "user43543@user.com";
        service.getCustomer(email);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomerInactive() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user1056@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        String email = "user1056@user.com";
        service.emailMap().put("user1056@user.com", customer);
        service.getCustomer(email);
    }

    @Test
    public void testUpdateCustomer() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        Customer updatedCustomer = service.updateCustomer(customer);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, updatedCustomer);
    }

    @Test
    public void testUpdateCustomerPhoneException1() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Customer cust = new Customer();
        cust.setEmail("user@user.com");
        cust.setPhoneNumber("123432");
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(cust);
        Customer updatedCustomer = service.updateCustomer(customer);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(0)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, updatedCustomer);
    }
    @Test(expected=CustomerNotFoundException.class)
    public void testUpdateCustomerNotFound() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user123098@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        service.updateCustomer(customer);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testUpdateCustomerInactive() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user2208@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        service.updateCustomer(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user234@user.com", "12432435253", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        String email = "user234@user.com";
        service.emailMap().put("user234@user.com", customer);
        Customer deleteCustomer = service.deleteCustomer(email);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(0)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, deleteCustomer);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testDeleteCustomerException() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        String email = "user901@user.com";
        service.deleteCustomer(email);
    }

    @Test(expected=CustomerNotFoundException.class)
    public void testDeleteCustomerInactive() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user43@user.com", "12432435234", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(ArgumentMatchers.anyString())).thenReturn(customer);
        Mockito.when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        String email = "user43@user.com";
        service.deleteCustomer(email);
    }
}
