package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;
	
	@PostMapping("/submit/{username}")
	public UserOrder submit(@PathVariable String username) {
		Cart cart = userService.getUserCartByUsername(username);
		return orderService.submitCart(cart);
	}
	
	@GetMapping("/history/{username}")
	public List<UserOrder> getOrdersForUser(@PathVariable String username) {
		return orderService.getOrdersByUsername(username);
	}
}
