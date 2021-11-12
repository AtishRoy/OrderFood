package com.mindtree.order.management.ordermanagementservice.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FireBase {

	public Identities identities;
	public String sign_in_provider;
}
