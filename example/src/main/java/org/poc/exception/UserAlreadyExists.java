package org.poc.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class UserAlreadyExists extends WebApplicationException{

	public UserAlreadyExists(String message) {
		super(message);
	}
	
	public UserAlreadyExists(final Response response) {
		super(response);
	}
}
