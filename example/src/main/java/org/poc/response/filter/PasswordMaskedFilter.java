package org.poc.response.filter;

import java.awt.TextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import org.poc.model.User;
import org.poc.repo.CustomRepository;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@PassMasked
public class PasswordMaskedFilter implements ContainerResponseFilter{
	
	CustomRepository customRepository = CustomRepository.getInstance();

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
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
				
				throw new IOException("User Not Valid");
			}
			else {
				String userPass = user.get(0).getPassword();
				TextField textField = new TextField(userPass);
				responseContext.getHeaders().add("Password", userPass.replaceAll("[a-zA-Z0-9^]", "*"));
			}
		}
		
	}

}
