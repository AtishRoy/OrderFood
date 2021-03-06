package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document(indexName = "subcategory", shards = 1, replicas = 0, refreshInterval = "5s", createIndex = false)
@ApiModel(value = "SubCategory", description = "SubCategory details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8405024480156314227L;
	@ApiModelProperty(notes = "Name of the category")
	private String subCategoryName;
	@ApiModelProperty(notes = "List of items")
	private List<Item> itemList;

}
