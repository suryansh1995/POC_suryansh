package org.poc.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

@Provider
public class Interceptor implements WriterInterceptor{

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		
		      OutputStream os = context.getOutputStream();
		     // os.write("Attache any output you want \n".getBytes());
		      context.proceed();		      
	}
}

