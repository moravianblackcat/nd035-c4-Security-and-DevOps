package com.example.demo.controller;

import com.example.demo.controller.base.CartControllerTestBase;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.service.exception.ItemWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class CartControllerAuthenticatedUserTest extends CartControllerTestBase {

    @Test
    public void postAddExistingItemForExistingUserAddsItemToCart() throws Exception {
        String username = "name";
        String itemName = "Item";
        String description = "description";
        String itemPrice = "2.00";
        String cartTotal = "4.00";
        int quantity = 2;
        long itemId = 2L;
        long userId = 1L;
        long cartId = 1L;

        ModifyCartRequest request = createRequest(username, itemId, quantity);

        User user = createUser(username, userId);
        Item item = createItem(itemId, itemName, description, itemPrice);
        when(itemService.findById(anyLong())).thenReturn(item);
        Cart empty = createCart(cartId, user, cartTotal, new ArrayList<>());
        when(userService.getUserCartByUsername(anyString())).thenReturn(empty);
        List<Item> cartItems = new ArrayList<>(List.of(item, item));
        Cart withItems = createCart(cartId, user, cartTotal, cartItems);
        when(cartService.addItemsAndSave(any(Cart.class), any(Item.class), anyInt())).thenReturn(withItems);

        mockMvc.perform(post(ADD_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id", is(2)))
                .andExpect(jsonPath("$.items[0].name", is(itemName)))
                .andExpect(jsonPath("$.items[0].price", is(2.0)))
                .andExpect(jsonPath("$.items[0].description", is(description)))
                .andExpect(jsonPath("$.user.id", is(1)))
                .andExpect(jsonPath("$.user.username", is(username)))
                .andExpect(jsonPath("$.total", is(4.0)));
    }

    @Test
    public void postAddItemWithNonExistingUserThrowsAnError() throws Exception {
        String username = "Non Existing User";
        ModifyCartRequest request = createRequest(username, 1L, 1);

        when(userService.getUserCartByUsername(username)).thenThrow(new UserWithThisUsernameWasNotFoundException());

        mockMvc.perform(post(ADD_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this username was not found."));
    }

    @Test
    public void postAddNonExistingItemThrowsAnError() throws Exception {
        String username = "username";
        long id = 15L;
        ModifyCartRequest request = createRequest(username, id, 22);

        when(itemService.findById(id)).thenThrow(new ItemWithThisIdWasNotFoundException());

        mockMvc.perform(post(ADD_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Item with this ID was not found."));
    }

    @Test
    public void postRemoveExistingItemForExistingUserAddsItemToCart() throws Exception {
        String username = "name";
        String itemName = "Item";
        String description = "description";
        String itemPrice = "2.00";
        String cartTotal = "4.00";
        int quantity = 2;
        long itemId = 2L;
        long userId = 1L;
        long cartId = 1L;

        ModifyCartRequest request = createRequest(username, itemId, quantity);

        User user = createUser(username, userId);
        Item item = createItem(itemId, itemName, description, itemPrice);
        when(itemService.findById(anyLong())).thenReturn(item);
        List<Item> cartItems = new ArrayList<>(List.of(item, item));
        Cart withItems = createCart(cartId, user, cartTotal, cartItems);
        when(userService.getUserCartByUsername(anyString())).thenReturn(withItems);
        Cart empty = createCart(cartId, user, cartTotal, new ArrayList<>());
        when(cartService.removeItemsAndSave(any(Cart.class), any(Item.class), anyInt())).thenReturn(empty);

        mockMvc.perform(post(REMOVE_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.user.id", is(1)))
                .andExpect(jsonPath("$.user.username", is(username)));
    }

    @Test
    public void postRemoveItemWithNonExistingUserThrowsAnError() throws Exception {
        String username = "Non Existing User";
        ModifyCartRequest request = createRequest(username, 1L, 1);

        when(userService.getUserCartByUsername(username)).thenThrow(new UserWithThisUsernameWasNotFoundException());

        mockMvc.perform(post(REMOVE_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this username was not found."));
    }

    @Test
    public void postRemoveExistingItemThrowsAnError() throws Exception {
        String username = "username";
        long id = 15L;
        ModifyCartRequest request = createRequest(username, id, 22);

        when(itemService.findById(id)).thenThrow(new ItemWithThisIdWasNotFoundException());

        mockMvc.perform(post(REMOVE_ITEM_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Item with this ID was not found."));
    }

    private ModifyCartRequest createRequest(String username, Long id, int quantity) {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(id);
        request.setQuantity(quantity);

        return request;
    }

    private Cart createCart(long cartId, User user, String total, List<Item> items) {
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUser(user);
        cart.setTotal(new BigDecimal(total));
        cart.setItems(items);
        return cart;
    }

    private User createUser(String username, Long id) {
        User user = new User();
        user.setUsername(username);
        user.setId(id);
        return user;
    }

    private Item createItem(long id, String itemName, String description, String price) {
        Item item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setName(itemName);
        item.setPrice(new BigDecimal(price));

        return item;
    }

}