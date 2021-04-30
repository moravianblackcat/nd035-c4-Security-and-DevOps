package com.example.demo.controller;

import com.example.demo.model.persistence.Item;
import com.example.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping
	public List<Item> getItems() {
		return itemService.findAll();
	}
	
	@GetMapping("/{id}")
	public Item getItemById(@PathVariable Long id) {
		return itemService.findById(id);
	}
	
	@GetMapping("/name/{name}")
	public List<Item> getItemsByName(@PathVariable String name) {
		return itemService.findByName(name);
			
	}
	
}
