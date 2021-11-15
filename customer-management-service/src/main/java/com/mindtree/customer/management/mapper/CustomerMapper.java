package com.mindtree.customer.management.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindtree.customer.management.dto.CustomerIdResponse;
import com.mindtree.customer.management.dto.CustomerRequest;
import com.mindtree.customer.management.dto.CustomerResponse;
import com.mindtree.customer.management.exception.CustomerNotFoundException;
import com.mindtree.customer.management.exception.InvalidDataFormatException;
import com.mindtree.customer.management.exception.InvalidDateFormatException;
import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.model.CustomerStatus;
import com.mindtree.customer.management.model.OAuthUser;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerMapper.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
public class CustomerMapper {

	/**
	 * log.
	 */
	private static final Logger log = LoggerFactory.getLogger(CustomerMapper.class);

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * buildCustomerResponse.
	 *
	 * &#64;param customer , not null.
	 * &#64;param message , not null.
	 * &#64;return customerResponse , never null.
	 * </pre>
	 */
	public static CustomerResponse buildCustomerResponse(final Customer customer, final String message) {
		CustomerResponse customerResponse = new CustomerResponse();
		if (customer.getAddress() != null) {
			customerResponse.setAddress(customer.getAddress().getAddress());
			customerResponse.setArea(customer.getAddress().getArea());
			customerResponse.setCity(customer.getAddress().getCity());
			customerResponse.setLandmark(customer.getAddress().getLandmark());
			customerResponse.setLatitude(customer.getAddress().getLatitude());
			customerResponse.setLongitude(customer.getAddress().getLongitude());
			customerResponse.setPinCode(customer.getAddress().getPinCode());
			customerResponse.setState(customer.getAddress().getState());
		}
		customerResponse.setCreatedDate(customer.getCreatedDate());
		if (customer.getDateOfBirth() != null) {
			customerResponse.setDateOfBirth(covertDateToString(customer.getDateOfBirth()));
		}
		customerResponse.setEmail(customer.getEmail());
		customerResponse.setFirstName(customer.getFirstName());
		customerResponse.setLastName(customer.getLastName());
		customerResponse.setPhoneNumber(customer.getPhoneNumber());
		customerResponse.setStatus(customer.getStatus());
		customerResponse.setMessage(message);
		return customerResponse;
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * covertDateToString.
	 *
	 * &#64;param date , not null.
	 * &#64;return date , never null.
	 * </pre>
	 */
	private static String covertDateToString(final Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(date);
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * buildCustomerForUpdateCustomer.
	 *
	 * &#64;param email , not null.
	 * &#64;param customerDto , not null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	public static Customer buildCustomerForUpdateCustomer(final String email, final CustomerRequest customerDto) {
		Customer customer = new Customer();
		Address address = new Address();
		address.setAddress(customerDto.getAddress().trim());
		address.setArea(customerDto.getArea().trim());
		address.setCity(customerDto.getCity().trim());
		if (customerDto.getLandmark() != null && !customerDto.getLandmark().trim().isEmpty()) {
			if (customerDto.getLandmark().trim().length() > 2 && customerDto.getLandmark().trim().length() < 51) {
				address.setLandmark(customerDto.getLandmark().trim());
			} else {
				throw new InvalidDataFormatException("Landmark can should contain 3-50 characters");
			}
		}
		address.setLatitude(customerDto.getLatitude().trim());
		address.setLongitude(customerDto.getLongitude().trim());
		validateNAddPinCode(customerDto.getPinCode().trim(), address);
		address.setState(customerDto.getState().trim());
		customer.setAddress(address);
		customer.setCreatedDate(new Date());
		mapDob(customerDto, customer);
		customer.setEmail(email);
		customer.setFirstName(customerDto.getFirstName().trim());
		customer.setLastName(customerDto.getLastName().trim());
		if (customerDto.getPhoneNumber().trim().matches("[0-9]+") && customerDto.getPhoneNumber().trim().length() == 10) {
			validateNAddPhoneNumber(customerDto.getPhoneNumber().trim(), customer);
		} else {
			throw new InvalidDataFormatException("Only numbers are accepted for Phone number with 10 digit length");
		}
		customer.setStatus(CustomerStatus.ACTIVE.name());
		return customer;
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * validateNAddPhoneNumber.
	 *
	 * &#64;param phone , not null.
	 * &#64;param customer , not null.
	 * </pre>
	 */
	private static void validateNAddPhoneNumber(final String phone, final Customer customer) {
		if (Character.getNumericValue(phone.charAt(0)) > 0) {
			customer.setPhoneNumber(phone);
		} else {
			throw new InvalidDataFormatException("Phone number provided is in incorrect.");
		}
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * validateNAddPinCode.
	 *
	 * &#64;param pin , not null.
	 * &#64;param address , not null.
	 * </pre>
	 */
	private static void validateNAddPinCode(final String pin, final Address address) {
		if (Character.getNumericValue(pin.charAt(0)) > 0) {
			address.setPinCode(pin);
		} else {
			throw new InvalidDataFormatException("Pin code provided is in incorrect.");
		}
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * mapDob.
	 *
	 * &#64;param customerDto , not null.
	 * &#64;param customer , not null.
	 * </pre>
	 */
	public static void mapDob(final CustomerRequest customerDto, Customer customer) {
		if (customerDto.getDateOfBirth() == null || customerDto.getDateOfBirth().trim().isEmpty()) {
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);
			Date dateOfBirth = sdf.parse(customerDto.getDateOfBirth().trim());
			if (dateOfBirth.before(new Date())) {
				if (calculateYears(dateOfBirth) > 150) {
					throw new InvalidDateFormatException("Customer age should be lessthan 150 years");
				}
				customer.setDateOfBirth(dateOfBirth);
			} else {
				throw new InvalidDateFormatException("DOB date provided should not be a future date.");
			}
		} catch (ParseException ex) {
			throw new InvalidDateFormatException("DOB date fromat provided is incorrect. Please provide date in dd-MM-yyyy format.");
		}
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * buildCustomerForNewCustomer.
	 *
	 * &#64;param email , not null.
	 * &#64;param customerDto , not null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	public static Customer buildCustomerForNewCustomer(final String email, final CustomerRequest customerDto) {
		Customer customer = new Customer();
		Address address = new Address();
		address.setAddress(customerDto.getAddress().trim());
		address.setAddressId(0L);
		address.setArea(customerDto.getArea().trim());
		address.setCity(customerDto.getCity().trim());
		if (customerDto.getLandmark() != null && !customerDto.getLandmark().trim().isEmpty()) {
			if (customerDto.getLandmark().trim().length() > 2 && customerDto.getLandmark().trim().length() < 51) {
				address.setLandmark(customerDto.getLandmark().trim());
			} else {
				throw new InvalidDataFormatException("Landmark can should contain 3-50 characters");
			}
		}
		address.setLatitude(customerDto.getLatitude().trim());
		address.setLongitude(customerDto.getLongitude().trim());
		validateNAddPinCode(customerDto.getPinCode().trim(), address);
		address.setState(customerDto.getState().trim());
		customer.setCustomerId(0L);
		customer.setAddress(address);
		customer.setCreatedDate(new Date());
		mapDob(customerDto, customer);
		customer.setEmail(email);
		customer.setFirstName(customerDto.getFirstName().trim());
		customer.setLastName(customerDto.getLastName().trim());
		if (customerDto.getPhoneNumber().trim().matches("[0-9]+") && customerDto.getPhoneNumber().trim().length() == 10) {
			validateNAddPhoneNumber(customerDto.getPhoneNumber().trim(), customer);
		} else {
			throw new InvalidDataFormatException("Only numbers are accepted for Phone number with 10 digit length");
		}
		customer.setStatus(CustomerStatus.ACTIVE.name());
		return customer;
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * calculateYears.
	 *
	 * &#64;param dateOfBirth , not null.
	 * &#64;return years , never null.
	 * </pre>
	 */
	public static int calculateYears(final Date dateOfBirth) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateOfBirth);
		int year1 = now.get(Calendar.YEAR);
		int year2 = dob.get(Calendar.YEAR);
		int age = year1 - year2;
		int month1 = now.get(Calendar.MONTH);
		int month2 = dob.get(Calendar.MONTH);
		if (month2 > month1) {
			age--;
		} else if (month1 == month2) {
			int day1 = now.get(Calendar.DAY_OF_MONTH);
			int day2 = dob.get(Calendar.DAY_OF_MONTH);
			if (day2 > day1) {
				age--;
			}
		}
		return age;
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * buildCustomerResponseList.
	 *
	 * &#64;param customerList , not null.
	 * &#64;return list , never null.
	 * </pre>
	 */
	public static List<CustomerResponse> buildCustomerResponseList(final List<Customer> customerList) {
		if (customerList != null && !customerList.isEmpty()) {
			List<CustomerResponse> customerResponseList = new ArrayList<>();
			for (Customer customer : customerList) {
				customerResponseList.add(buildCustomerResponse(customer, null));
			}
			return customerResponseList;
		}
		throw new CustomerNotFoundException("No Customers found for the given status");
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * validateStatus.
	 *
	 * &#64;param status , not null.
	 * </pre>
	 */
	public static void validateStatus(
			@Valid @NotNull(message = "Customer Status cannot be null or blank") @NotBlank(message = "Customer Status cannot be null or blank") @Pattern(regexp = "^[a-zA-Z]*$", message = "Invalid Status") final String status) {

		if (status != null && (status.trim().equalsIgnoreCase(CustomerStatus.ACTIVE.name()) || status.trim().equalsIgnoreCase(CustomerStatus.INACTIVE.name()))) {
			return;
		} else {
			throw new CustomerNotFoundException("Invalid Customer Status");
		}
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * getEmailFromToken.
	 *
	 * &#64;param user , not null.
	 * &#64;return email , never null.
	 * </pre>
	 */
	public static String getEmailFromToken(final OAuthUser user) {
		if (user != null && user.getFirebase() != null && user.getFirebase().getIdentities() != null && user.getFirebase().getIdentities().getEmail() != null
				&& !user.getFirebase().getIdentities().getEmail().isEmpty()) {
			log.debug("Email from Token: " + user.getFirebase().getIdentities().getEmail().get(0));
			return user.getFirebase().getIdentities().getEmail().get(0);
		}
		return "";
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * mapCustomerToCustomerIdResponse.
	 *
	 * &#64;param foundCustomer , may be null.
	 * &#64;return customerIdResponse , may be null.
	 * </pre>
	 */
	public static CustomerIdResponse mapCustomerToCustomerIdResponse(final Customer foundCustomer) {
		if (foundCustomer != null) {
			return new CustomerIdResponse(foundCustomer.getCustomerId().toString(), foundCustomer.getEmail());
		}
		return null;
	}
}
