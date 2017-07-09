package com.jbh.coupons.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbh.coupons.services.CouponSystemWebException;
import com.jbh.beans.Coupon;
import com.jbh.beans.Customer;
import com.jbh.enums.CouponType;
import com.jbh.facade.CustomerFacade;

@Path("/customers")
public class CustomerService {

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(Coupon coupon) {
		HttpSession session = request.getSession(false);
		CustomerFacade facade = (CustomerFacade) session.getAttribute("facade");
		try {
			facade.purchaseCoupon(coupon);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons() {
		HttpSession session = request.getSession(false);
		CustomerFacade facade = (CustomerFacade) session.getAttribute("facade");
		Collection<Coupon> list = new ArrayList<>();
		String listInString = null;
		try {
			list = facade.getAllPurchasedCoupons();
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(list);
			return Response.status(Status.OK).entity(listInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}

	}

	@GET
	@Path("/coupons/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(@PathParam("type") String type) {
		HttpSession session = request.getSession(false);
		CustomerFacade facade = (CustomerFacade) session.getAttribute("facade");
		Collection<Coupon> list = new ArrayList<>();
		String listInString = null;
		try {
			CouponType c = CouponType.valueOf(type);
			list = facade.getAllPurchasedCouponsByType(c);
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(list);
			return Response.status(Status.OK).entity(listInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}

	}

	@GET
	@Path("/coupons/price/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(@PathParam("price") double couponPrice) {
		HttpSession session = request.getSession(false);
		CustomerFacade facade = (CustomerFacade) session.getAttribute("facade");
		Collection<Coupon> list = new ArrayList<>();
		String listInString = null;
		try {
			list = facade.getAllPurchasedCouponsByPrice(couponPrice);
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(list);
			return Response.status(Status.OK).entity(listInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Customer customer) {
		CustomerFacade facade = new CustomerFacade();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		try {
			facade = (CustomerFacade) facade.login(customer.getName(), customer.getPassword());
			session = request.getSession(true);
			session.setAttribute("facade", facade);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
}
