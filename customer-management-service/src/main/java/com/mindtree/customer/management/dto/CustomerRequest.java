package com.mindtree.customer.management.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerVo.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Customer Request", description = "Customer Request details for create and update")
public class CustomerRequest {

	/**
	 * FirstName
	 */
	@NotNull(message = "Customer First Name cannot be null or blank")
	@NotBlank(message = "Customer First Name cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "Invalid First Name")
	@Length(min = 1, max = 50, message = "First name should contain 1-50 characters")
	@ApiModelProperty(required = true, notes = "First Name of the Customer")
	private String firstName;

	/**
	 * LastName.
	 */
	@NotNull(message = "Customer Last Name cannot be null or blank")
	@NotBlank(message = "Customer Last Name cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "Invalid Last Name")
	@Length(min = 1, max = 50, message = "Last name should contain 1-50 characters")
	@ApiModelProperty(required = true, notes = "Last Name of the Customer")
	private String lastName;

	/**
	 * PhoneNumber.
	 */
	@NotNull(message = "Customer Phone Number cannot be null or blank")
	@NotEmpty(message = "Customer Phone Number cannot be null or blank")
	@NotBlank(message = "Customer Phone Number cannot be null or blank")
	// @Pattern(regexp = "[0-9]+", message = "Only numbers are allowed for Phone Number")
	// @Length(min=10, max=10, message="Phone number should contain 10 numbers")
	@ApiModelProperty(required = true, notes = "Phone Number of the Customer")
	private String phoneNumber;

	/**
	 * DateOfBirth.
	 */
	@ApiModelProperty(required = false, notes = "DOB of the Customer in dd-MM-yyyy format")
	/*
	 * @Length(min=10, max=10, message="Date Of Birth should should be in the formate dd-MM-yyyy")
	 * 
	 * @Pattern(regexp="([0-9]{2})-([0-9]{2})-([0-9]{4})",
	 * message="Date Of Birth should should be in the formate dd-MM-yyyy")
	 */
	private String dateOfBirth;

	/**
	 * address.
	 */
	@NotNull(message = "Customer Address Line in Address cannot be null or blank")
	@NotBlank(message = "Customer Address in Address cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Address Line can contain  Alphanumeric values only")
	@Length(min = 5, max = 100, message = "Address Line should contain 5-100 characters")
	@ApiModelProperty(required = true, notes = "Address text of the Address")
	private String address;

	/**
	 * landmark.
	 */
	// @NotNull(message = "Customer Landmark in Address cannot be null")
	// @NotBlank(message = "Customer Landmark in Address cannot be blank")
	@Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Landmark can contain  Alphanumeric values only")
	// @Length(min=3, max=50, message="Landmark can should contain 3-50 characters")
	@ApiModelProperty(required = false, notes = "Landmark of the Address")
	private String landmark;

	/**
	 * area.
	 */
	@NotNull(message = "Customer Area in Address cannot be null or blank")
	@NotBlank(message = "Customer Area in Address cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "Area can contain  Alpha values only")
	@Length(min = 3, max = 30, message = "Area can should contain 3-30 characters")
	@ApiModelProperty(required = true, notes = "Area of the Address")
	private String area;

	/**
	 * city.
	 */
	@NotNull(message = "Customer City in Address cannot be null or blank")
	@NotBlank(message = "Customer City in Address cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "City can contain  Alpha values only")
	@Length(min = 3, max = 30, message = "City should contain 3-30 characters")
	@ApiModelProperty(required = true, notes = "City of the Address")
	private String city;

	/**
	 * state.
	 */
	@NotNull(message = "Customer State in Address cannot be null or blank")
	@NotBlank(message = "Customer State in Address cannot be null or blank")
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "State can contain  Alpha values only")
	@Length(min = 3, max = 20, message = "State should contain 3-20 characters")
	@ApiModelProperty(required = true, notes = "State of the Address")
	private String state;

	/**
	 * latitude.
	 */
	@NotNull(message = "Customer Latitude in Address cannot be null or blank")
	@NotBlank(message = "Customer Latitude in Address cannot be null or blank")
	@Pattern(regexp = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?[0-8]\\d((\\.)|\\.\\d{1,6})?)|(0*?90((\\.)|\\.0{1,6})?))$", message = "Invalid latitude format")
	@ApiModelProperty(required = true, notes = "Latitude of the Address")
	private String latitude;

	/**
	 * longitude.
	 */
	@NotNull(message = "Customer Longitude in Address cannot be null or blank")
	@NotBlank(message = "Customer Longitude in Address cannot be null or blank")
	@Pattern(regexp = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?\\d\\d((\\.)|\\.\\d{1,6})?)|(0*?1[0-7]\\d((\\.)|\\.\\d{1,6})?)|(0*?180((\\.)|\\.0{1,6})?))$", message = "Invalid longitude format")
	@ApiModelProperty(required = true, notes = "Longitude of the Address")
	private String longitude;

	/**
	 * pinCode.
	 */
	@NotNull(message = "Customer Pin Code in Address cannot be null or blank")
	@NotBlank(message = "Customer Pin Code in Address cannot be null or blank")
	@ApiModelProperty(required = true, notes = "Pin Code of the Address")
	@Pattern(regexp = "[0-9]+", message = "Only numbers are allowed for Pin Code")
	@Length(min = 6, max = 6, message = "Pin Code should be 6 digit length")
	private String pinCode;

	// @NotNull(message = "Customer Email cannot be null")
	// @NotBlank(message = "Customer Email cannot be blank")
	@ApiModelProperty(hidden = true)
	private String email;

}
