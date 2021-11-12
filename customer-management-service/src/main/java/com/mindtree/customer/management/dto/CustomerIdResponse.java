package com.mindtree.customer.management.dto;

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
 * @version $Revision: 1 $ $Date: 2018-09-24 10:50:55 PM $
 * @author $Author: nithya.pranesh $ 
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
    @ApiModelProperty(notes = "customerId of the Customer", hidden=true)
    private String customerId;
    
    /**
     * email.
     */
    @ApiModelProperty(notes = "Email of the Customer", hidden=true)
    private String email;

}
