package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(indexName = "item", type = "restaurant")
@ApiModel(value = "Item", description = "Item details") 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
	@ApiModelProperty(notes = "Name of the item")
	private String itemName;
	@ApiModelProperty(notes = "Price of the item")
	private float price;
	@ApiModelProperty(notes = "Description of the item")
	private String description;

}
