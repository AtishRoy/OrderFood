package com.mindtree.order.management.ordermanagementservice.model;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class CustomerResponse {

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private String dateOfBirth;

	private String address;

	private String landmark;

	private String area;

	private String city;

	private String state;

	private String latitude;

	private String longitude;
	private String pinCode;

	private String status;

	private Date createdDate;

	private String message;

}
