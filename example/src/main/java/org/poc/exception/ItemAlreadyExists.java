package org.poc.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ItemAlreadyExists extends WebApplicationException{
	
	public ItemAlreadyExists(Response response) {
		super(response);
	}

}
