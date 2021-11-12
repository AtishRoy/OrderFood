package com.mindtree.customer.management.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FireBase implements Serializable {

    private Identities identities;
    private String sign_in_provider;
}
