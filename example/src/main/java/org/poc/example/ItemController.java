package org.poc.example;

import org.poc.filter.DeleteRoleFilter;
import org.poc.filter.ItemRoleVerification;
import org.poc.filter.ProducerConsumer;
import org.poc.interceptor.ItemIdValidation;
import org.poc.model.Item;
import org.poc.repo.CustomRepository;
import org.poc.response.filter.PassMasked;
import org.poc.service.ItemService;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("item")
@Singleton
public class ItemController {
	
	CustomRepository customRepository = CustomRepository.getInstance();
	ItemService itemService = new ItemService();
	
	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ItemRoleVerification
	@ItemIdValidation
	public Response saveItem(Item item) {
		return Response.status(200).entity(itemService.addItem(item)).build();
	}
	
	@GET
	@Path("/get/allItems")
	public Response getAllItems() {
		return Response.status(200).entity(itemService.getAllItems()).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update/item")
	@ItemRoleVerification
	@ItemIdValidation
	public Response updateItem(Item item) {
		return Response.status(200).entity(itemService.updateItem(item)).build();
	}
	
	@GET
	@Path("/get-item-by-id/{id}")
	@ProducerConsumer
	@PassMasked
	public Response getItemById(@PathParam(value = "id") String id ) {
		return Response.status(200).entity(customRepository.getItemById(id)).build();
	}
	
	@DELETE
	@Path("/delete-item/{id}")
	@DeleteRoleFilter
	public Response deleteItem(@PathParam(value = "id") String id) {
		return Response.status(200).entity(customRepository.deleteItem(id)).build();
	}

}
