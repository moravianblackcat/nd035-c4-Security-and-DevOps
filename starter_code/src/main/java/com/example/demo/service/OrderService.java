package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;

import java.util.List;

public interface OrderService {

    List<UserOrder> getOrdersByUsername(String username);

    UserOrder submitCart(Cart cart);

}
