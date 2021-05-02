package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

public interface UserService {

    User createNewUser(String username, String password, String confirmPassword);

    User findById(long id);

    User findByUsername(String username);

    Cart getUserCartByUsername(String username);

    void setNewEmptyCartForTheUser(User user);
}
