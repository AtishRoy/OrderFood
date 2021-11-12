package com.mindtree.order.management.ordermanagementservice.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OAuthUser {

	public String aud;
	public String sub;
	public boolean email_verified;
	public String user_id;
	public int auth_time;
	public String iss;
	public String name;
	public int exp;
	public FireBase firebase;
}
