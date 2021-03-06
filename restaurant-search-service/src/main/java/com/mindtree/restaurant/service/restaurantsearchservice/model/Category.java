package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Document(indexName = "category", type = "category")
@ApiModel(value = "Category", description = "Category details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8470224279995198030L;
	@ApiModelProperty(notes = "Category name")
	private String categoryName;
	@ApiModelProperty(notes = "List of categories")
	private List<SubCategory> subCategoryList;

}
