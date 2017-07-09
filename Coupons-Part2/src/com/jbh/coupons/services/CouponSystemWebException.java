package com.jbh.coupons.services;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CouponSystemWebException extends WebApplicationException {
	
	public CouponSystemWebException(String message, Response.Status stat) {
		super(Response.status(stat).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
	
	public CouponSystemWebException(String message) {
		super(Response.status(Response.Status.CONFLICT).entity(message).
				type(MediaType.TEXT_PLAIN).build());
	}

	@Override
	public Response getResponse() {
		return super.getResponse();
	}

	}
