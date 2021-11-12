package com.mindtree.customer.management.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
public class CustomerServiceImplTest {
    
    @Autowired
    private CustomerServiceImpl service;
 
    @MockBean
    private CustomerRepository repository;
    
    /*@TestConfiguration
    public class ModifyCustomerManagementServiceTestContextConfiguration {
        @Bean
        public CustomerRepository globalSummitService() {
            return repository;
        }
    }*/
    
    @Before
    public void setUp() {
        service.setHazelcastCacheSwitch("FALSE");
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
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
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
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
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(repository.findByPhoneNumber(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        service.addCustomer(customer);
    }
    
    @Test
    public void testAddCustomerRecreate() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
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
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        service.addCustomer(customer);
    }
    
    @Test
    public void testGetCustomers() {
        List<Customer> custList = new ArrayList<Customer>();
        Mockito.when(repository.findByStatus(Mockito.anyString())).thenReturn(custList);
        String status = "ACTIVE";
        List<Customer> foundCustomer = service.getCustomers(status);
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(0)).findByEmail(customerArgument.capture());
        Assert.assertEquals(custList, foundCustomer);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomersException() {
        Mockito.when(repository.findByStatus(Mockito.anyString())).thenReturn(null);
        String status = "ACTIVE";
        service.getCustomers(status);
    }
    
    @Test
    public void testGetCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        String email = "user@user.com";
        Customer foundCustomer = service.getCustomer(email);
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(customerArgument.capture());
        Assert.assertEquals(customer, foundCustomer);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomerNotFound() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        String email = "user@user.com";
        service.getCustomer(email);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testGetCustomerInactive() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        String email = "user@user.com";
        service.getCustomer(email);
    }
    
    @Test
    public void testUpdateCustomer() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        Customer updatedCustomer = service.updateCustomer(customer);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, updatedCustomer);
    }
    
    @Test(expected=CustomerAlreadyExistsException.class)
    public void testUpdateCustomerPhoneException() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Customer cust = new Customer();
        cust.setEmail("asdsf");
        cust.setPhoneNumber("12432435");
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(Mockito.anyString())).thenReturn(cust);
        service.updateCustomer(customer);
    }
    @Test
    public void testUpdateCustomerPhoneException1() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Customer cust = new Customer();
        cust.setEmail("user@user.com");
        cust.setPhoneNumber("123432");
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        Mockito.when(repository.findByPhoneNumber(Mockito.anyString())).thenReturn(cust);
        Customer updatedCustomer = service.updateCustomer(customer);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, updatedCustomer);
    }
    @Test(expected=CustomerNotFoundException.class)
    public void testUpdateCustomerNotFound() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        service.updateCustomer(customer);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testUpdateCustomerInactive() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        service.updateCustomer(customer);
    }
    
    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        String email = "user@user.com";
        Customer deleteCustomer = service.deleteCustomer(email);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(emailArgument.capture());
        ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(repository, Mockito.times(1)).save(customerArgument.capture());
        Assert.assertEquals(customer, deleteCustomer);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testDeleteCustomerException() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        String email = "user@user.com";
        service.deleteCustomer(email);
    }
    
    @Test(expected=CustomerNotFoundException.class)
    public void testDeleteCustomerInactive() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        String email = "user@user.com";
        service.deleteCustomer(email);
    }
    
    @Test
    public void testGetCustomerForId() {

        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Customer foundCustomer = service.getCustomerForId("user@user.com");
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(customerArgument.capture());
        Assert.assertEquals(customer, foundCustomer);
    }
    
    @Test
    public void testGetCustomerForId1() {
        Customer customer = new Customer(1L, "firstname", "lastName", "user@user.com", "12432435", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE",
            new Date());
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Customer foundCustomer = service.getCustomerForId("user@user.com");
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(customerArgument.capture());
        Assert.assertEquals(null, foundCustomer);
    }
    
    @Test
    public void testGetCustomerForId2() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(null);
        Customer foundCustomer = service.getCustomerForId("user@user.com");
        ArgumentCaptor<String> customerArgument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(customerArgument.capture());
        Assert.assertEquals(null, foundCustomer);
    }
}
