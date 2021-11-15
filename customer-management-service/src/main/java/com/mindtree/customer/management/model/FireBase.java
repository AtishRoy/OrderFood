package com.mindtree.customer.management.model;

import java.io.Serializable;

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
public class FireBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1300735307018391058L;
	private Identities identities;
	private String sign_in_provider;
}
