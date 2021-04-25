package com.example.demo.service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.service.exception.UserWithThisIdeWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(UserWithThisIdeWasNotFoundException::new);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserWithThisUsernameWasNotFoundException();
        }

        return user;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UserWithThisUsernameAlreadyExistsException();
        }
        return userRepository.save(user);
    }
}