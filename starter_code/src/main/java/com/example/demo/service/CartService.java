package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;

public interface CartService {

    Cart addItemsAndSave(Cart cart, Item item, int quantity);

    Cart removeItemsAndSave(Cart cart, Item item, int quantity);

    Cart saveCart(Cart cart);

}
