package com.example.demo.controller;

import com.example.demo.controller.base.ItemControllerTestBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerUnauthenticatedUserTest extends ItemControllerTestBase {

    @Test
    public void unauthenticatedUserCannotGetItems() throws Exception {
        mockMvc.perform(get("/api/item"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthenticatedUserCannotGetItemById() throws Exception {
        mockMvc.perform(get("/api/item/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthenticatedUserCannotGetItemsByName() throws Exception {
        mockMvc.perform(get("/api/item/name/product"))
                .andExpect(status().isForbidden());
    }

}
