package com.mindtree.customer.management.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mindtree.customer.management.exception.CustomerAlreadyExistsException;
import com.mindtree.customer.management.exception.CustomerNotFoundException;
import com.mindtree.customer.management.model.Address;
import com.mindtree.customer.management.model.Customer;
import com.mindtree.customer.management.model.CustomerStatus;
import com.mindtree.customer.management.repository.CustomerRepository;
import com.mindtree.customer.management.service.CustomerService;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerServiceImpl.
 *
 * &#64;version $Revision: 1 $ $Date: 2018-09-23 09:49:07 AM $
 * &#64;author $Author: nithya.pranesh $
 * </pre>
 */
@RefreshScope
@Service
public class CustomerServiceImpl implements CustomerService {

	/**
	 * customerRepository.
	 */
	@Autowired
	private CustomerRepository customerRepository;

	@Value("${customer.management.phone.number.already.registered}")
	private String phoneNumberAlreadyRegistered;

	@Value("${customer.management.existing.customer}")
	private String existingCustomer;

	@Value("${customer.management.no.customer.for.email}")
	private String noCustomerForEmail;

	@Value("${customer.management.no.customer.for.status}")
	private String noCustomerForStatus;

	@Value("${customer.management.no.customer.for.email.update}")
	private String noCustomerForEmailUpdate;

	@Value("${customer.management.already.exist.with.phone.number}")
	private String alreadyExistsWithPhoneNumber;

	@Value("${customer.management.no.customer.for.email.delete}")
	private String noCustomerForEmailDelete;

	@Value("${HAZELCAST.CACHE.SWITCH}")
	private String hazelcastCacheSwitch;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * <pre>
     * <b>Description : </b>
     * Set the 'hazelcastCacheSwitch' attribute value.
     *
     * @param hazelcastCacheSwitchParam , may be null.
     * </pre>
     */
    public void setHazelcastCacheSwitch(final String hazelcastCacheSwitchParam) {
        this.hazelcastCacheSwitch = hazelcastCacheSwitchParam;
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * emailMap.
     *
     * @return emailMap , not null.
     * </pre>
     */
    public IMap<String, Customer> emailMap() {
        return hazelcastInstance.getMap("email");
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * phoneNumberMap.
     *
     * @return phoneNumberMap , not null.
     * </pre>
     */
    public IMap<String, Customer> phoneNumberMap() {
        return hazelcastInstance.getMap("phoneNumber");
    }

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * addCustomer.
	 *
	 * &#64;param customer , may be null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	@Override
	//@Transactional
    public Customer addCustomer(final Customer customer) {
        Customer findByEmail = getCustomerUsingEmail(customer.getEmail());
        if (findByEmail == null) {
            if (!isPhoneNumberPresent(customer.getPhoneNumber())) {
                addToEmailCacheMap(customer);
                addToPhoneNumberCacheMap(customer);
                return customerRepository.save(customer);
            }
            else {
                throw new CustomerAlreadyExistsException(phoneNumberAlreadyRegistered);
            }
        }
        else if (findByEmail.getStatus().equalsIgnoreCase(CustomerStatus.INACTIVE.name())) {
            replaceNewDataToCustomer(findByEmail, customer);
            findByEmail.setStatus(CustomerStatus.ACTIVE.name());
            updatedCustomerInMap(findByEmail);
            return customerRepository.save(findByEmail);
        }
        else {
            throw new CustomerAlreadyExistsException(existingCustomer);
        }
    }

	/**
	 * <pre>
	 * <b>Description : </b>
	 * getCustomer.
	 *
	 * &#64;param email , may be null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	@Override
	public Customer getCustomer(final String email) {
	    Customer foundCustomer = getCustomerUsingEmail(email);
	    if (foundCustomer == null) {
	        throw new CustomerNotFoundException(noCustomerForEmail);
	    }
	    else if (foundCustomer.getStatus().equalsIgnoreCase(CustomerStatus.INACTIVE.name())) {
	        throw new CustomerNotFoundException(noCustomerForEmail);
	    }
	    else {
	        return foundCustomer;
	    }
	}

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * getCustomers.
	 *
	 * &#64;param email , not null.
	 * &#64;param status , not null.
	 * &#64;return CustomerList , never null.
	 * </pre>
	 */
	@Override
    public List<Customer> getCustomers(final String status) {
        List<Customer> findByStatus = customerRepository.findByStatus(status.trim());
        if (findByStatus == null) {
            throw new CustomerNotFoundException(noCustomerForStatus);
        }
        return findByStatus;
    }

	/**
	 * <pre>
	 * <b>Description : </b>
	 * updateCustomer.
	 *
	 * &#64;param customer , may be null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	@Override
	//@Transactional
    public Customer updateCustomer(final Customer customer) {
        Customer findByEmail = getCustomerUsingEmail(customer.getEmail());
        if (findByEmail == null) {
            throw new CustomerNotFoundException(noCustomerForEmailUpdate);
        }
        else if (findByEmail.getStatus().equalsIgnoreCase(CustomerStatus.INACTIVE.name())) {
            throw new CustomerNotFoundException(noCustomerForEmailUpdate);
        }
        else {
            Customer findByPhoneNumber = getCustomerUsingPhoneNumber(customer.getPhoneNumber());
            if (findByPhoneNumber != null && !findByPhoneNumber.getEmail().equalsIgnoreCase(customer.getEmail())) {
                throw new CustomerAlreadyExistsException(alreadyExistsWithPhoneNumber);
            }
            replaceNewDataToCustomer(findByEmail, customer);
            updatedCustomerInMap(findByEmail);
            return customerRepository.save(findByEmail);
        }
    }

	/**
	 * <pre>
	 * <b>Description : </b>
	 * deleteCustomer.
	 *
	 * &#64;param email , may be null.
	 * &#64;return customer , never null.
	 * </pre>
	 */
	@Override
	//@Transactional
    public Customer deleteCustomer(final String email) {
        Customer findByEmail = getCustomerUsingEmail(email);
        if (findByEmail == null) {
            throw new CustomerNotFoundException(noCustomerForEmailDelete);
        }
        else if (findByEmail.getStatus().equalsIgnoreCase(CustomerStatus.INACTIVE.name())) {
            throw new CustomerNotFoundException(noCustomerForEmailDelete);
        }
        else {
            findByEmail.setStatus(CustomerStatus.INACTIVE.name());
            updatedCustomerInMap(findByEmail);
            return customerRepository.save(findByEmail);
        }
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * isPhoneNumberPresent.
     *
     * &#64;param phoneNumber , may be null.
     * &#64;return boolean , never null.
     * </pre>
     */
    private boolean isPhoneNumberPresent(final String phoneNumber) {
        if (getCustomerUsingPhoneNumber(phoneNumber) != null) {
            return true;
        }
        return false;
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * replaceNewDataToCustomer.
     *
     * &#64;param findByEmail , may be null.
     * &#64;param customer , may be null.
     * </pre>
     */
    private void replaceNewDataToCustomer(final Customer findByEmail, final Customer customer) {
        if (customer.getAddress() != null) {
            Address address = findByEmail.getAddress();
            address.setAddress(customer.getAddress().getAddress());
            address.setArea(customer.getAddress().getArea());
            address.setCity(customer.getAddress().getCity());
            address.setLandmark(customer.getAddress().getLandmark());
            address.setLatitude(customer.getAddress().getLatitude());
            address.setLongitude(customer.getAddress().getLongitude());
            address.setPinCode(customer.getAddress().getPinCode());
            address.setState(customer.getAddress().getState());
        }
        findByEmail.setDateOfBirth(customer.getDateOfBirth());
        findByEmail.setFirstName(customer.getFirstName());
        findByEmail.setLastName(customer.getLastName());
        findByEmail.setPhoneNumber(customer.getPhoneNumber());
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * addToEmailCacheMap.
     *
     * @param newCustomer , not null.
     * </pre>
     */
	private void addToEmailCacheMap(final Customer newCustomer) {
        emailMap().put(newCustomer.getEmail(), newCustomer);
    }

	/**
	 *
	 * <pre>
	 * <b>Description : </b>
	 * addToPhoneNumberCacheMap.
	 *
	 * @param newCustomer , not null.
	 * </pre>
	 */
    private void addToPhoneNumberCacheMap(final Customer newCustomer) {
        phoneNumberMap().put(newCustomer.getPhoneNumber(), newCustomer);
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * updatedCustomerInMap.
     *
     * @param updatedCustomer , not null.
     * </pre>
     */
    private void updatedCustomerInMap(final Customer updatedCustomer) {
        emailMap().replace(updatedCustomer.getEmail(), updatedCustomer);
        phoneNumberMap().replace(updatedCustomer.getPhoneNumber(), updatedCustomer);
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * getCustomerUsingEmail.
     *
     * @param email , may be null.
     * @return findByEmail , may be null.
     * </pre>
     */
    private Customer getCustomerUsingEmail(final String email) {
        if ("TRUE".equalsIgnoreCase(hazelcastCacheSwitch) && emailMap().containsKey(email)) {
            return emailMap().get(email);
        }
        else {
            Customer findByEmail = customerRepository.findByEmail(email);
            if (findByEmail != null) {
                addToEmailCacheMap(findByEmail);
                addToPhoneNumberCacheMap(findByEmail);
            }
            return findByEmail;
        }
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * getCustomerUsingPhoneNumber.
     *
     * @param phoneNumber , may be null.
     * @return findByPhoneNumber , may be null.
     * </pre>
     */
    private Customer getCustomerUsingPhoneNumber(final String phoneNumber) {
        if ("TRUE".equalsIgnoreCase(hazelcastCacheSwitch) && phoneNumberMap().containsKey(phoneNumber)) {
            return phoneNumberMap().get(phoneNumber);
        }
        else {
            Customer findByPhoneNumber = customerRepository.findByPhoneNumber(phoneNumber);
            if (findByPhoneNumber != null) {
                addToEmailCacheMap(findByPhoneNumber);
                addToPhoneNumberCacheMap(findByPhoneNumber);
            }
            return findByPhoneNumber;
        }
    }

    /**
     *
     * <pre>
     * <b>Description : </b>
     * getCustomerForId.
     *
     * @param email , may be null.
     * @return foundCustomer , may be null.
     * </pre>
     */
	@Override
	public Customer getCustomerForId(String email) {
		Customer foundCustomer = getCustomerUsingEmail(email);
		if (foundCustomer != null && foundCustomer.getStatus().equalsIgnoreCase(CustomerStatus.INACTIVE.name())) {
			foundCustomer = null;
		}
		return foundCustomer;
	}
}
