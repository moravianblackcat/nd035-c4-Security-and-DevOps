package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

public interface UserService {
    User findById(long id);

    User findByUsername(String username);

    Cart getUserCartByUsername(String username);

    User saveUser(User user);

    User createNewUser(String username);
}
