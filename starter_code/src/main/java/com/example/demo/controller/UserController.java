package com.example.demo.controller;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/id/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.findById(id);
	}
	
	@GetMapping("/{username}")
	public User getUserByUsername(@PathVariable String username) {
		return userService.findByUsername(username);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = userService.createNewUser(createUserRequest.getUsername());
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
}
