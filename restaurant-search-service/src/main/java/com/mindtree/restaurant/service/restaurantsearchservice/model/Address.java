package com.mindtree.restaurant.service.restaurantsearchservice.model;

import org.elasticsearch.common.geo.GeoPoint;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "Address", description = "Address details") 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	@ApiModelProperty(notes = "Address of the restaurant")
	private String addressLine;
	@ApiModelProperty(notes = "Landmark near to Restaurant")
	private String landmark;
	@ApiModelProperty(notes = "Area where the Restaurant is located")
	private String area;
	@ApiModelProperty(notes = "City where the Restaurant is located")
	private String city;
	@ApiModelProperty(notes = "state where the Restaurant is located")
	private String state;	
	@ApiModelProperty(notes = "Pincode of the restaurant")
	private String pinCode;
	@ApiModelProperty(notes = "latitude of restaurant")
    private GeoPoint location;
    /*@ApiModelProperty(notes = "longitutde of restaurant")
    private float longitutde;*/

}
