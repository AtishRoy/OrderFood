package com.mindtree.customer.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindtree.customer.management.model.Customer;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerRepository.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	Customer findByEmail(@Param("email") String email);

	Customer findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	List<Customer> findByStatus(@Param("status") String status);

}
