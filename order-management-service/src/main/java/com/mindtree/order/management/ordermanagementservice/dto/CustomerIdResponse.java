package com.mindtree.order.management.ordermanagementservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerIdResponse.
 *
 * &#64;version $Revision: 1 $ $Date: 2018-09-24 10:50:55 PM $
 * &#64;author $Author: nithya.pranesh $
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Customer ID Response", description = "Customer ID Response details")
public class CustomerIdResponse {

	/**
	 * customerId.
	 */
	@ApiModelProperty(notes = "customerId of the Customer")
	private String customerId;

	/**
	 * email.
	 */
	@ApiModelProperty(notes = "Email of the Customer")
	private String email;

}
