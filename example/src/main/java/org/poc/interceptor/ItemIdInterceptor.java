package org.poc.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;

@Provider
@ItemIdValidation
public class ItemIdInterceptor implements ReaderInterceptor {

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		InputStream is = context.getInputStream();
		JsonObject json = Json.createReader(is).readObject();
System.out.println("ITEM json is "+json.getString("itemId").length());
		System.out.println("PRICE "+(json.getInt("price") > 10000));
		if(json.getString("itemId").length() > 6 || json.getString("itemId").length()<6) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Item ID should be of 6 letters").build();
			throw new WebApplicationException(response);
		}else if (!Character.isLetter(json.getString("itemId").charAt(0))) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("First letter of ItemID should be alphabet").build();
			throw new WebApplicationException(response);
		}else if (!Character.isDigit(json.getString("itemId").charAt(1)) || !Character.isDigit(json.getString("itemId").charAt(2)) || !Character.isDigit(json.getString("itemId").charAt(3))
				|| !Character.isDigit(json.getString("itemId").charAt(4)) || !Character.isDigit(json.getString("itemId").charAt(5))) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("After First letter remaining should be digit only").build();
			throw new WebApplicationException(response);
		}else if (json.getString("itemId").isBlank()) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("itemId can't be blank").build();
			throw new WebApplicationException(response);
		}else if(json.getString("name").isBlank()) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("name can't be blank").build();
			throw new WebApplicationException(response);
		}else if (json.getInt("price") > 10000 || json.getInt("price") < 0) {
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Price should be between 0 and 10000").build();
			throw new WebApplicationException(response);
		}
		is = new ByteArrayInputStream(json.toString().getBytes());
		context.setInputStream(is);
		System.out.println(is);
		return context.proceed();
	}

}
