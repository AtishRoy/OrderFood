package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OAuthUser implements Serializable {

	private static final long serialVersionUID = 2484102415228114143L;
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
