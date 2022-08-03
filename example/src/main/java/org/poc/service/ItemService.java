package org.poc.service;

import java.util.ArrayList;
import java.util.List;

import org.poc.model.Item;
import org.poc.repo.CustomRepository;

public class ItemService {
	
	CustomRepository customRepository = CustomRepository.getInstance();
	
	public Item addItem(Item item) {
		System.out.println("Reached AddITem in Service Class");
		Item resultItem = customRepository.saveItem(item);
		return resultItem;
	}
	
	public List<Item> getAllItems(){
		
		List<Item> items = new ArrayList<Item>();
		items = (List) customRepository.getAllItems();
		
		return items;
	}

	public Item updateItem(Item item) {
		// TODO Auto-generated method stub
		List<Item> savedItem = customRepository.getItemById(item.getItemId());
		savedItem.get(0).setDescription(item.getDescription());
		savedItem.get(0).setName(item.getName());
		savedItem.get(0).setPrice(item.getPrice());
		customRepository.updateItem(item);
		return item;
	}
}
