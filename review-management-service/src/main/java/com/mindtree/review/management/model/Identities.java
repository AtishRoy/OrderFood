package com.mindtree.review.management.model;

import java.io.Serializable;
import java.util.List;

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
public class Identities implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7202423713397855563L;
	private List<String> name;
    private List<String> email;
}
