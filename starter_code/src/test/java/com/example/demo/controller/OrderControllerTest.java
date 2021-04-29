package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import com.example.demo.service.exception.YouCannotSubmitAnEmptyCart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private final String SUBMIT_ENDPOINT = "/api/order/submit/ghost";
    private final String HISTORY_ENDPOINT = "/api/order/history/ghost";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Test
    public void orderSubmitReturnsOverview() throws Exception {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        Item item = new Item();
        item.setId(2L);
        String name = "Widget";
        item.setName(name);
        BigDecimal price = new BigDecimal("2.0");
        item.setPrice(price);
        String description = "Description";
        item.setDescription(description);
        userOrder.setItems(List.of(item));
        userOrder.setTotal(price);

        when(orderService.submitCart(any())).thenReturn(userOrder);

        mockMvc.perform(post(SUBMIT_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id", is(2)))
                .andExpect(jsonPath("$.items[0].name", is(name)))
                .andExpect(jsonPath("$.items[0].price", is(2.0)))
                .andExpect(jsonPath("$.items[0].description", is(description)))
                .andExpect(jsonPath("$.total", is(2.0)));
    }

    @Test
    public void orderSubmitForNonExistingUserThrowsAnError() throws Exception {
        when(userService.getUserCartByUsername(anyString())).thenThrow(new UserWithThisUsernameWasNotFoundException());

        mockMvc.perform(post(SUBMIT_ENDPOINT))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this username was not found."));
    }

    @Test
    public void orderSubmitForAnEmptyCartThrowsAnError() throws Exception {
        Cart empty = new Cart();

        when(userService.getUserCartByUsername(anyString())).thenReturn(empty);
        when(orderService.submitCart(any(Cart.class))).thenThrow(new YouCannotSubmitAnEmptyCart());

        mockMvc.perform(post(SUBMIT_ENDPOINT))
                .andExpect(status().isNotAcceptable())
                .andExpect(status().reason("You cannot submit an empty cart."));
    }

    @Test
    public void orderHistoryShowsAllInfo() throws Exception {
        User user = new User();
        user.setId(1L);
        String username = "ghost";
        user.setUsername(username);

        Item item = new Item();
        item.setId(1L);
        String name = "Item name";
        item.setName(name);
        item.setPrice(new BigDecimal("2.0"));
        String description = "Item Description";
        item.setDescription(description);

        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(List.of(item));
        order.setTotal(new BigDecimal("2.0"));

        when(orderService.getOrdersByUsername(anyString())).thenReturn(List.of(order));

        mockMvc.perform(get(HISTORY_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].items", hasSize(1)))
                .andExpect(jsonPath("$.[0].items[0].id", is(1)))
                .andExpect(jsonPath("$.[0].items[0].name", is(name)))
                .andExpect(jsonPath("$.[0].items[0].price", is(2.0)))
                .andExpect(jsonPath("$.[0].items[0].description", is(description)));
    }

    @Test
    public void orderHistoryForNonExistingUserThrowsAnError() throws Exception {
        when(orderService.getOrdersByUsername(anyString())).thenThrow(new UserWithThisUsernameWasNotFoundException());

        mockMvc.perform(get(HISTORY_ENDPOINT))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this username was not found."));
    }
}