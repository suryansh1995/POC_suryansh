package org.poc.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class UserNotValid extends WebApplicationException{
	
	public UserNotValid(String message) {
		super(message);
	}
	
	public UserNotValid(Response response) {
		super(response);
	}

}
