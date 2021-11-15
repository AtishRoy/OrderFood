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
@Document(indexName = "cuisine", shards = 1, replicas = 0, refreshInterval = "5s", createIndex = false)
@ApiModel(value = "Cuisine", description = "Cuisine details") 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cuisine implements Serializable {
	
	@ApiModelProperty(notes = "Cuisines available in Restaurant")
	private String cuisineName;

}
