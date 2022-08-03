package org.poc.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;

@Provider
@BlankCheck
public class BlankCheckInterceptor implements ReaderInterceptor {
	
	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		InputStream is = context.getInputStream();
		JsonObject json = Json.createReader(is).readObject();
		System.out.println("json is" + json);
		if (json.getString("name").isBlank()) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("User Name cannot be Empty").build();
			throw new WebApplicationException(response);
		} else if (json.getString("role").isBlank()) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Role cannot be Empty")
					.build();
			throw new WebApplicationException(response);
		}else if (json.getString("password").isBlank()) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Password cannot be Empty")
					.build();
			throw new WebApplicationException(response);
		}
		is = new ByteArrayInputStream(json.toString().getBytes());
		context.setInputStream(is);
		System.out.println(is);
		return context.proceed();
	}
}
