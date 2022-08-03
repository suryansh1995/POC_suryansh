package org.poc.example;



import org.poc.exception.UserAlreadyExists;
import org.poc.filter.RoleVerification;
import org.poc.interceptor.BlankCheck;
import org.poc.model.User;
import org.poc.repo.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/data")
@Singleton
public class UserController {

	
	CustomRepository customRepository = CustomRepository.getInstance();
		
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RoleVerification
    @BlankCheck
    public Response addUser(User data) throws UserAlreadyExists {
    	
    	customRepository.saveUser(data);
    	return Response.ok(data).build();
    }
    	
    @Path("/get/user/{name}")
    @GET
    public Response getUserByName(@PathParam(value = "name") String name) {
    	return Response.ok(customRepository.getbyName(name)).build();
	}
    
    @GET
    @Path("/get/users")
    public Response getAllUser() {
    	return Response.status(200).entity(customRepository.getAllUsers()).build();
    }
    
    @POST
    @Path("/add/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAdmin(User user) {
    	return Response.status(200).entity(customRepository.saveAdminUser(user)).build();
    }
}
