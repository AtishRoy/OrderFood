package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(indexName = "restaurant", type = "restaurant", shards = 10, replicas = 0, refreshInterval = "-1")
@ApiModel(value = "Restaurant", description = "Restaurant details")
@Api(value = "Search Restaurant API")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant implements Serializable {
    private static final long serialVersionUID = 4282487172478696150L;
    @Id
    @ApiModelProperty(notes = "Id of the restaurant")
    private String restaurantId;
    @ApiModelProperty(notes = "Name of the restaurant")
    private String name;
    @ApiModelProperty(notes = "Address of the restaurant")
    private Address address;
    @ApiModelProperty(notes = "Website of the restaurant")
    private String websiteLink;
    @ApiModelProperty(notes = "Cusisines Available in restaurant the restaurant")
    private Collection<Cuisine> cuisineList;
    @ApiModelProperty(notes = "Phone number of the restaurant")
    private String phoneNumber;
    @ApiModelProperty(notes = "Average Cost per 2 of the restaurant")
    private float budget;
    @ApiModelProperty(notes = "Restaurant service start time of the restaurant")
    private String openingTime;
    @ApiModelProperty(notes = "Restaurant service end time  of the restaurant")
    private String closingTime;
    /*
     * @ApiModelProperty(notes = "Latitude location of the restaurant") private
     * String lat_location;
     * 
     * @ApiModelProperty(notes = "Longitude location of the restaurant") private
     * String lon_location;
     */
    @Field(type = FieldType.Nested, includeInParent = true)
    @ApiModelProperty(notes = "Category of the restaurant")
    private Collection<Category> categoryList;
    @ApiModelProperty(notes = "Restaurant Rating")
    private float overallRating;

}
