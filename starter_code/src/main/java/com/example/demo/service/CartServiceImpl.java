package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart addItemsAndSave(Cart cart, Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.addItem(item);
            cart.addPrice(item.getPrice());
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemsAndSave(Cart cart, Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.removeItem(item);
            cart.removePrice(item.getPrice());
        }
        return cartRepository.save(cart);
    }
}
