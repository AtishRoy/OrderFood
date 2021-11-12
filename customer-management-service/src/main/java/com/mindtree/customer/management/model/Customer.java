package com.mindtree.customer.management.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * <b>Description : </b>
 * Customer.
 * 
 * @version $Revision: 1 $ $Date: 2018-09-23 05:53:35 PM $
 * @author $Author: nithya.pranesh $ 
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ApiModel(value = "Customer", description = "Customer details") 
public class Customer implements Serializable {
    
	/**
	 * CustomerID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	@ApiModelProperty(required = true, notes = "Id of the Customer")
	private Long customerId;
	
	/**
	 * FirstName.
	 */
	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "Customer First Name cannot be null")
	@ApiModelProperty(required = true, notes = "First Name of the Customer")
	private String firstName;
	
	/**
	 * LastName.
	 */
	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Customer Last Name cannot be null")
	@ApiModelProperty(required = true, notes = "Last Name of the Customer")
	private String lastName;
	
	/**
	 * Email.
	 */
	@Column(name = "email", unique = true, nullable = false)
	@NotBlank(message = "Customer Email cannot be null")
	@ApiModelProperty(required = true, notes = "Email of the Customer")
	private String email;
	
	/**
	 * PhoneNumber.
	 */
	@Column(name = "phone_number", unique = true, nullable = false)
	@NotBlank(message = "Customer Phone Number cannot be null")
	@ApiModelProperty(required = true, notes = "Phone Number of the Customer")
	private String phoneNumber;
	
	/**
	 * DateOfBirth.
	 */
	@Column(name = "date_of_birth", nullable = true)
	@ApiModelProperty(required = false, notes = "DOB of the Customer")
	private Date dateOfBirth;
	
	/**
	 * Addresses.
	 */
	@JoinColumn(name = "address", nullable = true)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	/*@JoinTable(name = "customer_address", joinColumns=@JoinColumn(name="customer_id"),
	inverseJoinColumns=@JoinColumn(name="address_id"))*/
	@ApiModelProperty(required = false, notes = "Addresses of the Customer")
	private Address address;

	/**
	 * AccountStatus.
	 */
	@Column(name = "status", nullable = false)
	@ApiModelProperty(required = true, notes = "Status of the Customer")
	private String status;
	
	/**
	 * AccountCreationDate.
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", nullable = false)
	@ApiModelProperty(required = true, notes = "Creation Date of the Customer Account")
	private Date createdDate;
	
}
