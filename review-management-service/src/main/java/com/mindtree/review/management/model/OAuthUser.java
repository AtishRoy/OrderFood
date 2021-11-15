package com.mindtree.review.management.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OAuthUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1878798313196795816L;
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
