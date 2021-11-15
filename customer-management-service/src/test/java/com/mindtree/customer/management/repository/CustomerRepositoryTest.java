package com.mindtree.customer.management.repository;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerRepositoryTest.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class CustomerRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CustomerRepository repository;

	@Before
	public void setup() {
		entityManager.clear();
		Customer customer = new Customer(2L, "firstname", "lastName", "user@example.com", "12432435", new Date(),
				new Address(2L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE", new Date());
		entityManager.merge(customer);
		entityManager.flush();
		Customer customer1 = new Customer(3L, "firstname", "lastName", "user1@example.com", "124324351", new Date(),
				new Address(3L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "INACTIVE", new Date());
		entityManager.merge(customer1);
		entityManager.flush();
	}

	@After
	public void remove() {
		entityManager.clear();
	}

	@Test
	public void testFindByEmail() {
		Customer findByEmailCustomer = repository.findByEmail("user1@example.com");
		Assert.assertNotNull(findByEmailCustomer);
		Assert.assertEquals("user1@example.com", findByEmailCustomer.getEmail());
	}

	@Test
	public void testFindByPhoneNumberl() {
		Customer findByEmailCustomer = repository.findByPhoneNumber("12432435");
		Assert.assertNotNull(findByEmailCustomer);
		Assert.assertEquals("user@example.com", findByEmailCustomer.getEmail());
	}

	@Test
	public void testFindByStatus() {
		List<Customer> findByEmailCustomer = repository.findByStatus("INACTIVE");
		Assert.assertNotNull(findByEmailCustomer);
		Assert.assertEquals(1, findByEmailCustomer.size());
	}

}
