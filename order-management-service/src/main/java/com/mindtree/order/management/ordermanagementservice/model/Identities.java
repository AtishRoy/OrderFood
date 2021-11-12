package com.mindtree.order.management.ordermanagementservice.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Identities {

	public List<String> name;
	public List<String> email;
}
