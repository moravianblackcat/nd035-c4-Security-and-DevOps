package com.example.demo.service;

import com.example.demo.model.persistence.User;

public interface UserService {

    User findByUsername(String username);

    User saveUser(User user);

}
