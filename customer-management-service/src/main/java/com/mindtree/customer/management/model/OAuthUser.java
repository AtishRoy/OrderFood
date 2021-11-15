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
public class OAuthUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5864715360373839041L;
	private String aud;
	private String sub;
	private boolean email_verified;
	private String user_id;
	private int auth_time;
	private String iss;
	private String name;
	private int exp;
	private FireBase firebase;
}
