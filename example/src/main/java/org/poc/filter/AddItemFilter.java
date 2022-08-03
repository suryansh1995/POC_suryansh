package org.poc.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import org.poc.exception.UserNotValid;
import org.poc.model.User;
import org.poc.repo.CustomRepository;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@ItemRoleVerification
public class AddItemFilter implements ContainerRequestFilter{
	
	CustomRepository customRepository = CustomRepository.getInstance();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		
		List<String> authHeader = requestContext.getHeaders().get("Authorization");
		
		if (authHeader!=null && authHeader.size()>0) {
			String authToken = authHeader.get(0);
			String decordeString = new String (Base64.getDecoder().decode(authToken.substring(6).getBytes()));
			StringTokenizer tokenizer = new StringTokenizer(decordeString,":");
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			System.out.println("USERNAME "+username);
			List<User> user = new ArrayList<User>();
			user = customRepository.getbyName(username);
			System.out.println("Reached addItem filter "+user.get(0).getName());
			if(Objects.isNull(user)) {
				
				throw new UserNotValid(Response.status(400).entity("User Not Valid").build());
			}
			else if (Objects.nonNull(user) && user.get(0).getRole().equalsIgnoreCase("producer")) {
				return;
			} else {
				Response UnAutorizedResource = Response.status(Response.Status.UNAUTHORIZED)
						.entity("Only Producer can add Item")
						.build();
				requestContext.abortWith(UnAutorizedResource);
			}
		}
	}

}
