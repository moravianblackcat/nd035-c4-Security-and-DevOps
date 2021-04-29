package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.service.exception.YouCannotSubmitAnEmptyCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Override
    public UserOrder submitCart(Cart cart) {
        if (cart.isEmpty()) {
            throw new YouCannotSubmitAnEmptyCart();
        }
        UserOrder order = createOrderFromCart(cart);

        return orderRepository.save(order);
    }

    @Override
    public List<UserOrder> getOrdersByUsername(String username) {
        User user = userService.findByUsername(username);

        return orderRepository.findByUser(user);
    }

    private UserOrder createOrderFromCart(Cart cart) {
        UserOrder order = new UserOrder();
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());

        return order;
    }
}
