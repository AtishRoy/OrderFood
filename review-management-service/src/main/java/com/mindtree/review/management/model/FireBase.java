package com.mindtree.review.management.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FireBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4808951991765305872L;
	private Identities identities;
	private String sign_in_provider;
}
