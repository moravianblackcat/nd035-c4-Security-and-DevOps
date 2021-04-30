package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;
	
	@PostMapping("/addToCart")
	public Cart addToCart(@RequestBody ModifyCartRequest request) {
		Cart cart = userService.getUserCartByUsername(request.getUsername());
		Item item = itemService.findById(request.getItemId());

		return cartService.addItemsAndSave(cart, item, request.getQuantity());
	}
	
	@PostMapping("/removeFromCart")
	public Cart removeFromCart(@RequestBody ModifyCartRequest request) {
		Cart cart = userService.getUserCartByUsername(request.getUsername());
		Item item = itemService.findById(request.getItemId());

		return cartService.removeItemsAndSave(cart, item, request.getQuantity());
	}
		
}
