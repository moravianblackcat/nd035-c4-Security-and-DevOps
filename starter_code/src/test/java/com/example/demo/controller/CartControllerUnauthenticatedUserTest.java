package com.example.demo.controller;

import com.example.demo.controller.base.CartControllerTestBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerUnauthenticatedUserTest extends CartControllerTestBase {

    @Test
    public void unauthenticatedUserCannotAddToCart() throws Exception {
        mockMvc.perform(post("/api/cart/addToCart"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthenticatedUserCannotRemoveFromCart() throws Exception {
        mockMvc.perform(post("/api/cart/removeFromCart"))
                .andExpect(status().isForbidden());
    }

}
