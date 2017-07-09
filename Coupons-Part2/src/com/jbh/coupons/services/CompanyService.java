package com.jbh.coupons.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import com.jbh.beans.Company;
import com.jbh.beans.Coupon;
import com.jbh.enums.CouponType;
import com.jbh.facade.CompanyFacade;

@Path("/companies")
public class CompanyService {
	@Context
	private HttpServletRequest request;

	@POST
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCoupon(Coupon coupon) {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		try {
			facade.createCoupon(coupon);
			return Response.status(Status.CREATED).build();
		} catch (Exception e) {
			throw new CouponSystemWebException("CompanyService: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCoupon(Coupon coupon) {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		try {
			facade.removeCoupon(coupon);
			return Response.status(Status.NO_CONTENT).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCoupon(Coupon coupon) {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		try {
			facade.updateCoupon(coupon);
			return Response.status(Status.NO_CONTENT).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NOT_MODIFIED);
		}
	}

	@GET
	@Path("/coupons/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoupon(@PathParam("id") long id) {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		Coupon coupon = new Coupon();
		String couponInString = null;
		try {
			coupon = facade.getCoupon(id);
			if (coupon.getTitle() == null)
				throw new SQLException();
			ObjectMapper mapper = new ObjectMapper();
			couponInString = mapper.writeValueAsString(coupon);
			return Response.status(Status.OK).entity(couponInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NOT_MODIFIED);
		}

	}

	@GET
	@Path("/coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupons() throws Exception {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		Collection<Coupon> list = new ArrayList<>();
		String listInString = null;
		try {
			list = facade.getAllCoupons();
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(list);
			return Response.status(Status.OK).entity(listInString).type(MediaType.APPLICATION_JSON).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("/coupons/type/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCouponByType(@PathParam("type") String couponType) {
		HttpSession session = request.getSession(false);
		CompanyFacade facade = (CompanyFacade) session.getAttribute("facade");
		Collection<Coupon> couponsByType = new ArrayList<>();
		String listInString = null;
		try {
			CouponType myType = CouponType.valueOf(couponType);
			couponsByType = facade.getCouponByType(myType);
			if (couponsByType.size() == 0)
				return Response.status(Status.NO_CONTENT).build();
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(couponsByType);
			return Response.status(Status.OK).entity(listInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NOT_MODIFIED);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Company company) {
		CompanyFacade facade = new CompanyFacade();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		try {
			facade = (CompanyFacade) facade.login(company.getName(), company.getPassword());
			session = request.getSession(true);
			session.setAttribute("facade", facade);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
}
