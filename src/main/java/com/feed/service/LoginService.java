package com.feed.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.feed.util.LoginMockHelper;

@Path("/login")
public class LoginService {
	
	@GET
	@Path("/{userid}")
	public Response login(@PathParam("userid") String userid) {
		
		String output = ""+LoginMockHelper.userLogin(userid);
		return Response.status(200).entity(output).build();
	}
	
	
}
