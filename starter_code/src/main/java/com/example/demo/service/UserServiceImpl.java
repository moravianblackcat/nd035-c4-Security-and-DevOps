package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.service.exception.InvalidPasswordException;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import com.example.demo.service.strategy.PasswordCreationStrategy;
import com.example.demo.service.strategy.PasswordValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordValidationStrategy passwordValidationStrategy;

    @Autowired
    private PasswordCreationStrategy passwordCreationStrategy;

    @Override
    public User createNewUser(String username, String password, String confirmPassword) {
        User user = createUser(username);
        setPasswordToUser(password, confirmPassword, user);
        addEmptyCartToUser(user);

        return userRepository.save(user);
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

    @Override
    public void setNewEmptyCartForTheUser(User user) {
        Cart cart = new Cart();
        cartRepository.save(cart);

        user.setCart(cart);
        userRepository.save(user);
    }

    private User createUser(String username) {
        if (userRepository.existsUserByUsername(username)) {
            throw new UserWithThisUsernameAlreadyExistsException();
        }
        User user = new User();
        user.setUsername(username);
        return user;
    }

    private void setPasswordToUser(String password, String confirmPassword, User user) {
        if (passwordValidationStrategy.isValid(password, confirmPassword)) {
            user.setPassword(passwordCreationStrategy.generateEncodedPassword(password));
        } else {
            throw new InvalidPasswordException();
        }
    }

    private void addEmptyCartToUser(User user) {
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
    }

}