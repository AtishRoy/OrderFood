/*
 * This code contains copyright information which is the proprietary property
 * of SITA Information Network Computing Limited (SITA). No part of this
 * code may be reproduced, stored or transmitted in any form without the prior
 * written permission of SITA.
 *
 * Copyright (C) SITA Information Network Computing Limited 2010-2011.
 * All rights reserved.
 */
package com.mindtree.customer.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * <pre>
 * <b>Description : </b>
 * InvalidDateFormatException.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class InvalidDateFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6233010824537681902L;

	public InvalidDateFormatException(String message) {
		super(message);
	}

}
