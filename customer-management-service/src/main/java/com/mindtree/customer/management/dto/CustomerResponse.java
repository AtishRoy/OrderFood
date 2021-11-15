package com.mindtree.customer.management.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * <b>Description : </b>
 * CustomerResponse.
 *
 * @version $Revision: 1 $ $Date: 2018-09-24 12:48:56 AM $
 * @author $Author: nithya.pranesh $
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "Customer Response", description = "Customer Response details for create and update")
public class CustomerResponse {

    /**
     * FirstName
     */
    @ApiModelProperty(notes = "First Name of the Customer")
    private String firstName;

    /**
     * LastName.
     */
    @ApiModelProperty(notes = "Last Name of the Customer")
    private String lastName;

    /**
     * Email.
     */
    @ApiModelProperty(notes = "Email of the Customer")
    private String email;

    /**
     * PhoneNumber.
     */
    @ApiModelProperty(notes = "Phone Number of the Customer")
    private String phoneNumber;

    /**
     * DateOfBirth.
     */
    @ApiModelProperty(notes = "DOB of the Customer in dd-MM-yyyy format")
    private String dateOfBirth;

    /**
     * address.
     */
    @ApiModelProperty(notes = "Address text of the Address")
    private String address;

    /**
     * landmark.
     */
    @ApiModelProperty(notes = "Landmark of the Address")
    private String landmark;

    /**
     * area.
     */
    @ApiModelProperty(notes = "Area of the Address")
    private String area;

    /**
     * city.
     */
    @ApiModelProperty(notes = "City of the Address")
    private String city;

    /**
     * state.
     */
    @ApiModelProperty(notes = "State of the Address")
    private String state;

    /**
     * latitude.
     */
    @ApiModelProperty(notes = "Latitude of the Address")
    private String latitude;

    /**
     * longitude.
     */
    @ApiModelProperty(notes = "Longitude of the Address")
    private String longitude;

    /**
     * pinCode.
     */
    @ApiModelProperty(notes = "Pin Code of the Address")
    private String pinCode;

    /**
     * status.
     */
    @ApiModelProperty(notes = "Status of the Customer")
    private String status;

    /**
     * createdDate.
     */
    @ApiModelProperty(notes = "Creation Date of the Customer Account")
    private Date createdDate;

    @ApiModelProperty(notes = "Message for the operation performed")
    private String message;

}
