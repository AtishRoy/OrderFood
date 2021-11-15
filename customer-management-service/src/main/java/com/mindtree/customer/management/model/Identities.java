package com.mindtree.customer.management.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Identities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6217852063606517782L;
	private List<String> name;
	private List<String> email;
}
