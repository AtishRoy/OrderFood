package com.mindtree.customer.management.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindtree.customer.management.dto.CustomerIdResponse;
import com.mindtree.customer.management.dto.CustomerRequest;
import com.mindtree.customer.management.dto.CustomerResponse;
import com.mindtree.customer.management.exception.CustomerNotFoundException;
import com.mindtree.customer.management.exception.InvalidDataFormatException;
import com.mindtree.customer.management.exception.InvalidDateFormatException;
import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.model.FireBase;
import com.mindtree.customer.management.model.Identities;
import com.mindtree.customer.management.model.OAuthUser;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerServiceImplTest.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@RunWith(SpringRunner.class)
public class MapperTest {

	@TestConfiguration
	static class MapperTestContextConfiguration {
		@Bean
		public CustomerMapper customerMapper() {
			return new CustomerMapper();
		}
	}

	@Test
	public void testBuildCustomerResponse() {
		Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
				new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE", new Date());
		CustomerResponse buildCustomerResponse = CustomerMapper.buildCustomerResponse(customer, "Sucess");
		Assert.assertEquals("address", buildCustomerResponse.getAddress());
		Assert.assertEquals("area", buildCustomerResponse.getArea());
		Assert.assertEquals("city", buildCustomerResponse.getCity());
		Assert.assertEquals("email", buildCustomerResponse.getEmail());
		Assert.assertEquals("firstname", buildCustomerResponse.getFirstName());
		Assert.assertEquals("landmark", buildCustomerResponse.getLandmark());
		Assert.assertEquals("lastName", buildCustomerResponse.getLastName());
		Assert.assertEquals("+12.3456", buildCustomerResponse.getLatitude());
		Assert.assertEquals("-12.3456", buildCustomerResponse.getLongitude());
		Assert.assertEquals("Sucess", buildCustomerResponse.getMessage());
		Assert.assertEquals("12432435", buildCustomerResponse.getPhoneNumber());
		Assert.assertEquals("123456", buildCustomerResponse.getPinCode());
		Assert.assertEquals("ACTIVE", buildCustomerResponse.getStatus());
		Assert.assertEquals("state", buildCustomerResponse.getState());
	}

	@Test
	public void testBuildCustomerResponse1() {
		Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", null, null, "ACTIVE", new Date());
		CustomerResponse buildCustomerResponse = CustomerMapper.buildCustomerResponse(customer, "Sucess");
		Assert.assertNull(buildCustomerResponse.getAddress());
		Assert.assertNull(buildCustomerResponse.getArea());
		Assert.assertNull(buildCustomerResponse.getCity());
		Assert.assertEquals("email", buildCustomerResponse.getEmail());
		Assert.assertEquals("firstname", buildCustomerResponse.getFirstName());
		Assert.assertNull(buildCustomerResponse.getLandmark());
		Assert.assertEquals("lastName", buildCustomerResponse.getLastName());
		Assert.assertNull(buildCustomerResponse.getLatitude());
		Assert.assertNull(buildCustomerResponse.getLongitude());
		Assert.assertEquals("Sucess", buildCustomerResponse.getMessage());
		Assert.assertEquals("12432435", buildCustomerResponse.getPhoneNumber());
		Assert.assertNull(buildCustomerResponse.getPinCode());
		Assert.assertEquals("ACTIVE", buildCustomerResponse.getStatus());
		Assert.assertNull(buildCustomerResponse.getState());
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomerPhoneExce() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "244354353", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test
	public void testBuildCustomerForUpdateCustomer() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForUpdateCustomer = CustomerMapper.buildCustomerForUpdateCustomer("email", customerReq);
		Assert.assertNull(buildCustomerForUpdateCustomer.getCustomerId());
		Assert.assertEquals("email", buildCustomerForUpdateCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForUpdateCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForUpdateCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForUpdateCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForUpdateCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForUpdateCustomer.getAddress().getAddress());
		Assert.assertNull(buildCustomerForUpdateCustomer.getAddress().getAddressId());
		Assert.assertEquals("area", buildCustomerForUpdateCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForUpdateCustomer.getAddress().getCity());
		Assert.assertEquals("land", buildCustomerForUpdateCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForUpdateCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForUpdateCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForUpdateCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForUpdateCustomer.getAddress().getState());

	}

	@Test
	public void testBuildCustomerForUpdateCustomer5() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForUpdateCustomer = CustomerMapper.buildCustomerForUpdateCustomer("email", customerReq);
		Assert.assertNull(buildCustomerForUpdateCustomer.getCustomerId());
		Assert.assertEquals("email", buildCustomerForUpdateCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForUpdateCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForUpdateCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForUpdateCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForUpdateCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForUpdateCustomer.getAddress().getAddress());
		Assert.assertNull(buildCustomerForUpdateCustomer.getAddress().getAddressId());
		Assert.assertEquals("area", buildCustomerForUpdateCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForUpdateCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForUpdateCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForUpdateCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForUpdateCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForUpdateCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForUpdateCustomer.getAddress().getState());
	}

	@Test
	public void testBuildCustomerForUpdateCustomer6() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", null, "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForUpdateCustomer = CustomerMapper.buildCustomerForUpdateCustomer("email", customerReq);
		Assert.assertNull(buildCustomerForUpdateCustomer.getCustomerId());
		Assert.assertEquals("email", buildCustomerForUpdateCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForUpdateCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForUpdateCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForUpdateCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForUpdateCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForUpdateCustomer.getAddress().getAddress());
		Assert.assertNull(buildCustomerForUpdateCustomer.getAddress().getAddressId());
		Assert.assertEquals("area", buildCustomerForUpdateCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForUpdateCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForUpdateCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForUpdateCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForUpdateCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForUpdateCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForUpdateCustomer.getAddress().getState());
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomer8() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "a", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomer9() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address",
				"aasosadljasfxojfoaewjfohewfdiagefiagfigfugsairegfiregfieorgfoiregioerhogregoferjoigdeoirhgiehgieigehigheihgireshfdieorhdiorehfigeorgoewhgwe", "area", "city", "state", "latitude",
				"longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test
	public void testBuildCustomerForCreateCustomer5() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForNewCustomer = CustomerMapper.buildCustomerForNewCustomer("email", customerReq);
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getCustomerId().longValue());
		Assert.assertEquals("email", buildCustomerForNewCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForNewCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForNewCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForNewCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForNewCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForNewCustomer.getAddress().getAddress());
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getAddress().getAddressId().longValue());
		Assert.assertEquals("area", buildCustomerForNewCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForNewCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForNewCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForNewCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForNewCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForNewCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForNewCustomer.getAddress().getState());
	}

	@Test
	public void testBuildCustomerForCreateCustomer6() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", null, "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForNewCustomer = CustomerMapper.buildCustomerForNewCustomer("email", customerReq);
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getCustomerId().longValue());
		Assert.assertEquals("email", buildCustomerForNewCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForNewCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForNewCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForNewCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForNewCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForNewCustomer.getAddress().getAddress());
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getAddress().getAddressId().longValue());
		Assert.assertEquals("area", buildCustomerForNewCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForNewCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForNewCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForNewCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForNewCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForNewCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForNewCustomer.getAddress().getState());
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForCreateCustomer8() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "a", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForNewCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForCreateCustomer9() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address",
				"aasosadljasfxojfoaewjfohewfdiagefiagfigfugsairegfiregfieorgfoiregioerhogregoferjoigdeoirhgiehgieigehigheihgireshfdieorhdiorehfigeorgoewhgwe", "area", "city", "state", "latitude",
				"longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForNewCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomerPhone() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "0243243556", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomerPin() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "243243556", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "023432", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomer1() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "124", "22-10-1989", "address", null, "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForUpdateCustomer2() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "12432dfgrdr", "22-10-1989", "address", "", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForUpdateCustomer("Sucess", customerReq);
	}

	@Test
	public void testBuildCustomerForNewCustomer() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", null, "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForNewCustomer = CustomerMapper.buildCustomerForNewCustomer("email", customerReq);
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getCustomerId().longValue());
		Assert.assertEquals("email", buildCustomerForNewCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForNewCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForNewCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForNewCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForNewCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForNewCustomer.getAddress().getAddress());
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getAddress().getAddressId().longValue());
		Assert.assertEquals("area", buildCustomerForNewCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForNewCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForNewCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForNewCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForNewCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForNewCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForNewCustomer.getAddress().getState());
	}

	@Test
	public void testBuildCustomerForNewCustomer1() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForNewCustomer = CustomerMapper.buildCustomerForNewCustomer("email", customerReq);
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getCustomerId().longValue());
		Assert.assertEquals("email", buildCustomerForNewCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForNewCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForNewCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForNewCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForNewCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForNewCustomer.getAddress().getAddress());
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getAddress().getAddressId().longValue());
		Assert.assertEquals("area", buildCustomerForNewCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForNewCustomer.getAddress().getCity());
		Assert.assertNull(buildCustomerForNewCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForNewCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForNewCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForNewCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForNewCustomer.getAddress().getState());
	}

	@Test
	public void testBuildCustomerForNewCustomer2() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		Customer buildCustomerForNewCustomer = CustomerMapper.buildCustomerForNewCustomer("email", customerReq);
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getCustomerId().longValue());
		Assert.assertEquals("email", buildCustomerForNewCustomer.getEmail());
		Assert.assertEquals("sds", buildCustomerForNewCustomer.getFirstName());
		Assert.assertEquals("sdfds", buildCustomerForNewCustomer.getLastName());
		Assert.assertEquals("1243243556", buildCustomerForNewCustomer.getPhoneNumber());
		Assert.assertEquals("ACTIVE", buildCustomerForNewCustomer.getStatus());
		Assert.assertEquals("address", buildCustomerForNewCustomer.getAddress().getAddress());
		Assert.assertEquals(0L, buildCustomerForNewCustomer.getAddress().getAddressId().longValue());
		Assert.assertEquals("area", buildCustomerForNewCustomer.getAddress().getArea());
		Assert.assertEquals("city", buildCustomerForNewCustomer.getAddress().getCity());
		Assert.assertEquals("land", buildCustomerForNewCustomer.getAddress().getLandmark());
		Assert.assertEquals("latitude", buildCustomerForNewCustomer.getAddress().getLatitude());
		Assert.assertEquals("longitude", buildCustomerForNewCustomer.getAddress().getLongitude());
		Assert.assertEquals("pinCode", buildCustomerForNewCustomer.getAddress().getPinCode());
		Assert.assertEquals("state", buildCustomerForNewCustomer.getAddress().getState());
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForNewCustomerPhone() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "12432556", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForNewCustomer("dsfsd", customerReq);
	}

	@Test(expected = InvalidDataFormatException.class)
	public void testBuildCustomerForNewCustomerPhone1() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "werew", "22-10-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.buildCustomerForNewCustomer("dsfsd", customerReq);
	}

	@Test
	public void testValidateStatus() {
		CustomerMapper.validateStatus("ACTIVE");
	}

	@Test
	public void testValidateStatusValid1() {
		CustomerMapper.validateStatus("INACTIVE");
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testValidateStatusInvalid() {
		CustomerMapper.validateStatus("asfdsaf");
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testValidateStatusInvalid1() {
		CustomerMapper.validateStatus("");
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testValidateStatusInvalid2() {
		CustomerMapper.validateStatus(null);
	}

	@Test
	public void testBuildCustomerResponseList() {
		List<Customer> lsit = new ArrayList<>();
		Customer customer1 = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
				new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE", new Date());
		Customer customer2 = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
				new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE", new Date());
		lsit.add(customer1);
		lsit.add(customer2);
		List<CustomerResponse> buildCustomerResponseList = CustomerMapper.buildCustomerResponseList(lsit);
		Assert.assertEquals(2, buildCustomerResponseList.size());
		CustomerResponse buildCustomerResponse = buildCustomerResponseList.get(0);
		Assert.assertEquals("address", buildCustomerResponse.getAddress());
		Assert.assertEquals("area", buildCustomerResponse.getArea());
		Assert.assertEquals("city", buildCustomerResponse.getCity());
		Assert.assertEquals("email", buildCustomerResponse.getEmail());
		Assert.assertEquals("firstname", buildCustomerResponse.getFirstName());
		Assert.assertEquals("landmark", buildCustomerResponse.getLandmark());
		Assert.assertEquals("lastName", buildCustomerResponse.getLastName());
		Assert.assertEquals("+12.3456", buildCustomerResponse.getLatitude());
		Assert.assertEquals("-12.3456", buildCustomerResponse.getLongitude());
		Assert.assertEquals("12432435", buildCustomerResponse.getPhoneNumber());
		Assert.assertEquals("123456", buildCustomerResponse.getPinCode());
		Assert.assertEquals("ACTIVE", buildCustomerResponse.getStatus());
		Assert.assertEquals("state", buildCustomerResponse.getState());

		CustomerResponse buildCustomerResponse1 = buildCustomerResponseList.get(1);
		Assert.assertEquals("address", buildCustomerResponse1.getAddress());
		Assert.assertEquals("area", buildCustomerResponse1.getArea());
		Assert.assertEquals("city", buildCustomerResponse1.getCity());
		Assert.assertEquals("email", buildCustomerResponse1.getEmail());
		Assert.assertEquals("firstname", buildCustomerResponse1.getFirstName());
		Assert.assertEquals("landmark", buildCustomerResponse1.getLandmark());
		Assert.assertEquals("lastName", buildCustomerResponse1.getLastName());
		Assert.assertEquals("+12.3456", buildCustomerResponse1.getLatitude());
		Assert.assertEquals("-12.3456", buildCustomerResponse1.getLongitude());
		Assert.assertEquals("12432435", buildCustomerResponse1.getPhoneNumber());
		Assert.assertEquals("123456", buildCustomerResponse1.getPinCode());
		Assert.assertEquals("ACTIVE", buildCustomerResponse1.getStatus());
		Assert.assertEquals("state", buildCustomerResponse1.getState());
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testBuildCustomerResponseListNull() {
		CustomerMapper.buildCustomerResponseList(null);
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testBuildCustomerResponseListempty() {
		List<Customer> lsit = new ArrayList<>();
		CustomerMapper.buildCustomerResponseList(lsit);
	}

	@Test
	public void testmapDob() {
		CustomerMapper.mapDob(new CustomerRequest(), null);
	}

	@Test
	public void testmapDobblank() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.mapDob(customerReq, null);
	}

	@Test(expected = InvalidDateFormatException.class)
	public void testmapDobblank1() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "asdsa", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.mapDob(customerReq, null);
	}

	@Test(expected = InvalidDateFormatException.class)
	public void testmapDobblank2() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-08-1234", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.mapDob(customerReq, null);
	}

	@Test(expected = InvalidDateFormatException.class)
	public void testmapDobblank3() {
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-08-9999", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.mapDob(customerReq, null);
	}

	@Test
	public void testmapDobblank4() {
		Customer customer = new Customer();
		CustomerRequest customerReq = new CustomerRequest("sds", "sdfds", "1243243556", "22-08-1989", "address", "land", "area", "city", "state", "latitude", "longitude", "pinCode", "user");
		CustomerMapper.mapDob(customerReq, customer);
		Assert.assertNotNull(customer.getDateOfBirth());
	}

	@Test
	public void testCalculateYears() {
		CustomerMapper.calculateYears(new Date());
	}

	@Test
	public void testCalculateYears1() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			Date dateOfBirth = sdf.parse("22-08-1989");
			CustomerMapper.calculateYears(dateOfBirth);
		} catch (ParseException e) {
			Assert.fail();
		}
	}

	@Test
	public void testCalculateYears2() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			Date dateOfBirth = sdf.parse("22-10-1989");
			CustomerMapper.calculateYears(dateOfBirth);
		} catch (ParseException e) {
			Assert.fail();
		}
	}

	@Test
	public void testCalculateYears3() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			Date dateOfBirth = sdf.parse("30-09-1989");
			CustomerMapper.calculateYears(dateOfBirth);
		} catch (ParseException e) {
			Assert.fail();
		}
	}

	@Test
	public void testgetEmailFromToken() {
		OAuthUser user = new OAuthUser();
		FireBase firebase = new FireBase();
		Identities identities = new Identities();
		List<String> email = new ArrayList<>();
		email.add("email");
		identities.setEmail(email);
		firebase.setIdentities(identities);
		user.setFirebase(firebase);
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("email", emailFromToken);
	}

	@Test
	public void testgetEmailFromTokenEmpty() {
		OAuthUser user = new OAuthUser();
		FireBase firebase = new FireBase();
		Identities identities = new Identities();
		List<String> email = new ArrayList<>();
		identities.setEmail(email);
		firebase.setIdentities(identities);
		user.setFirebase(firebase);
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("", emailFromToken);
	}

	@Test
	public void testgetEmailFromTokenNoEmailList() {
		OAuthUser user = new OAuthUser();
		FireBase firebase = new FireBase();
		Identities identities = new Identities();
		firebase.setIdentities(identities);
		user.setFirebase(firebase);
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("", emailFromToken);
	}

	@Test
	public void testgetEmailFromTokenNoIdentiy() {
		OAuthUser user = new OAuthUser();
		FireBase firebase = new FireBase();
		user.setFirebase(firebase);
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("", emailFromToken);
	}

	@Test
	public void testgetEmailFromTokenNoFirebase() {
		OAuthUser user = new OAuthUser();
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("", emailFromToken);
	}

	@Test
	public void testgetEmailFromTokenNouser() {
		OAuthUser user = null;
		String emailFromToken = CustomerMapper.getEmailFromToken(user);
		Assert.assertEquals("", emailFromToken);
	}

	@Test
	public void testMapCustomerToCustomerIdResponse() {
		Customer customer = new Customer(1L, "firstname", "lastName", "email", "12432435", new Date(),
				new Address(1L, "address", "landmark", "area", "city", "state", "+12.3456", "-12.3456", "123456"), "ACTIVE", new Date());
		CustomerIdResponse customerIdResponse = CustomerMapper.mapCustomerToCustomerIdResponse(customer);
		Assert.assertEquals("1", customerIdResponse.getCustomerId());
		Assert.assertEquals("email", customerIdResponse.getEmail());
	}

	@Test
	public void testMapCustomerToCustomerIdResponse2() {
		CustomerIdResponse customerIdResponse = CustomerMapper.mapCustomerToCustomerIdResponse(null);
		Assert.assertNull(customerIdResponse);
	}
}
