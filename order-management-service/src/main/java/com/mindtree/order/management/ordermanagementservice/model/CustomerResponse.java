package com.mindtree.order.management.ordermanagementservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
