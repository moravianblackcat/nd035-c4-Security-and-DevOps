package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public User createNewUser(String username) {
        User user = new User();
        user.setUsername(username);
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);

        return saveUser(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(UserWithThisIdWasNotFoundException::new);
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
    public Cart getUserCartByUsername(String username) {
        return findByUsername(username).getCart();
    }

    private User saveUser(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UserWithThisUsernameAlreadyExistsException();
        }
        return userRepository.save(user);
    }
}