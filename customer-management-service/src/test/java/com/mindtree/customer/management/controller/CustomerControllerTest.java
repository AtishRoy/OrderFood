package com.mindtree.customer.management.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import com.mindtree.customer.management.CustomerManagementServiceApplication;
import com.mindtree.customer.management.dto.CustomerRequest;
import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.serviceimpl.CustomerServiceImpl;

/**
 * <pre>
 * <b>Description : </b>
 * <<WRITE DESCRIPTION HERE>>
 *
 * @version $Revision: 1 $ $Date: 2018-09-25 01:40:37 AM $
 * @author $Author: nithya.pranesh $
 * </pre>
 */
@RunWith(SpringRunner.class)
//@WebAppConfiguration
@SpringBootTest(classes = CustomerManagementServiceApplication.class)
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    MappingJackson2HttpMessageConverter jacksonConverter;

    @MockBean
    public CustomerServiceImpl service;

    @TestConfiguration
    public class ModifyCustomerManagementServiceTestContextConfiguration {
        @Bean
        public CustomerServiceImpl globalSummitService() {
            return service;
        }
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "2443543531", "22-10-1989", "address", "land",
            "area", "city", "state", "+12.123456", "+12.123456", "123456", "asdwfe");
        Mockito.when(service.addCustomer(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.body(convertToJson(customerReq));
        request.header("Content-Type", "application/json");
        request.when().post("/customer/").then().statusCode(201);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        Mockito.when(service.getCustomer(ArgumentMatchers.anyString())).thenReturn(customer);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.header("Content-Type", "application/json");
        request.when().get("/customer/").then().statusCode(200);
    }

    @Test
    public void testGetCustomers() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        List<Customer> list = new ArrayList<>();
        list.add(customer);
        Mockito.when(service.getCustomers(ArgumentMatchers.anyString())).thenReturn(list);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.header("Content-Type", "application/json");
        request.when().get("/customer/getByStatus/{status}", "ACTIVE").then().statusCode(200);
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "2443543531", "22-10-1989", "address", "land",
            "area", "city", "state", "+12.123456", "+12.123456", "123456", "dshiwf384");
        Mockito.when(service.updateCustomer(ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.body(convertToJson(customerReq));
        request.header("Content-Type", "application/json");
        request.when().put("/customer/").then().statusCode(200);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        Mockito.when(service.deleteCustomer(ArgumentMatchers.anyString())).thenReturn(customer);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.header("Content-Type", "application/json");
        request.when().delete("/customer/").then().statusCode(200);
    }

    @Test
    public void testGetCustomerWithId() {
        Customer customer = new Customer(1L, "firstname", "lastName", "email", "1243243598", new Date(),
            new Address(1L, "address", "landmark", "area", "city", "state", "+12.345623", "-12.345623", "123456"), "ACTIVE",
            new Date());
        Mockito.when(service.getCustomer(ArgumentMatchers.anyString())).thenReturn(customer);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
        request.header("Content-Type", "application/json");
        request.header("X-ACCESS-TOKEN", "token");
        request.when().get("/customer/getCustomerId").then().statusCode(200);
    }


    private String convertToJson(CustomerRequest customer) {

        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        try {
            jacksonConverter.write(customer, MediaType.APPLICATION_JSON, outputMessage);
            System.out.println(outputMessage.getBody().toString());
            return outputMessage.getBody().toString();
        }
        catch (HttpMessageNotWritableException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
