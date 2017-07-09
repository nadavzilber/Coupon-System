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
import com.jbh.beans.Customer;
import com.jbh.facade.AdminFacade;
import com.jbh.coupons.services.GenericUser;

@Path("/admin")
public class AdminService {
	@Context
	private HttpServletRequest request;

	@POST
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCompany(Company company) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.createCompany(company);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCompany(Company company) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.removeCompany(company);
			return Response.status(Status.NO_CONTENT).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@PUT
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.updateCompany(company);
			return Response.status(Status.NO_CONTENT).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/companies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompany(@PathParam("id") long id) throws Exception {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		Company company = new Company();
		String companyInString = null;
		try {
			company = facade.getCompany(id);
			if (company.getName() == null)
				throw new SQLException();
			ObjectMapper mapper = new ObjectMapper();
			companyInString = mapper.writeValueAsString(company);
			System.out.println(companyInString);
			return Response.status(Status.OK).entity(companyInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NO_CONTENT);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("/companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies() throws Exception {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		Collection<Company> list = new ArrayList<>();
		String listInString = "";
		try {
			list = facade.getAllCompanies();
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
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.createCustomer(customer);
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}

	@DELETE
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCustomer(Customer customer) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.removeCustomer(customer);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer) {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		try {
			facade.updateCustomer(customer);
			return Response.status(Status.NO_CONTENT).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/customers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("id") long id) throws Exception {
		HttpSession session = request.getSession(false);
		AdminFacade facade = (AdminFacade) session.getAttribute("facade");
		Customer customer;
		String customerInString = null;
		try {
			customer = facade.getCustomer(id);
			if (customer.getName() == null)
				throw new SQLException();
			ObjectMapper mapper = new ObjectMapper();
			customerInString = mapper.writeValueAsString(customer);
			System.out.println(customerInString);
			return Response.status(Status.OK).entity(customerInString).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NO_CONTENT);
		} catch (JsonProcessingException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@GET
	@Path("customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllCustomers() throws Exception {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		HttpSession session = request.getSession(false);
		AdminFacade af = (AdminFacade) session.getAttribute("facade");
		String listInString = null;
		try {
			customers = (ArrayList<Customer>) af.getAllCustomers();
			ObjectMapper mapper = new ObjectMapper();
			listInString = mapper.writeValueAsString(customers);
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
	public Response login(GenericUser admin) {
		AdminFacade facade = new AdminFacade();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		try {
			facade = (AdminFacade) facade.login(admin.getName(), admin.getPassword());
			session = request.getSession(true);
			session.setAttribute("facade", facade);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}
}
