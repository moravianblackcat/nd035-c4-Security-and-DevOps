package com.example.demo.controller;

import com.example.demo.controller.base.ItemControllerTestBase;
import com.example.demo.model.persistence.Item;
import com.example.demo.service.exception.ItemWithThisIdWasNotFoundException;
import com.example.demo.service.exception.ItemWithThisNameWasNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class ItemControllerAuthenticatedUserTest extends ItemControllerTestBase {

    @Test
    public void getItemsReturnsAllItems() throws Exception {
        List<Item> items = getDummyItems();

        when(itemService.findAll()).thenReturn(items);

        mockMvc.perform(get("/api/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Window")))
                .andExpect(jsonPath("$[0].description", is("Best window.")))
                .andExpect(jsonPath("$[0].price", is(21.13)));
    }

    @Test
    public void getItemByIdReturnsItemIfExists() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(new BigDecimal("2.54"));

        when(itemService.findById(1L)).thenReturn(item);

        mockMvc.perform(get("/api/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Item")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.price", is(2.54)));
    }

    @Test
    public void getNonExistingItemByIdThrowsAnError() throws Exception {
        when(itemService.findById(1L)).thenThrow(new ItemWithThisIdWasNotFoundException());

        mockMvc.perform(get("/api/item/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Item with this ID was not found."));
    }

    @Test
    public void getItemsByNameReturnsAllItems() throws Exception {
        when(itemService.findByName(anyString())).thenReturn(getDummyItems());

        mockMvc.perform(get("/api/item/name/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Window")))
                .andExpect(jsonPath("$[0].description", is("Best window.")))
                .andExpect(jsonPath("$[0].price", is(21.13)));
    }

    @Test
    public void getItemsByNameThrowsAnErrorIfNoneExists() throws Exception {
        when(itemService.findByName(anyString())).thenThrow(new ItemWithThisNameWasNotFoundException());

        mockMvc.perform(get("/api/item/name/name"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Item with this name was not found."));
    }



    private List<Item> getDummyItems() {
        Item window = new Item();
        window.setId(1L);
        window.setName("Window");
        window.setDescription("Best window.");
        window.setPrice(new BigDecimal("21.13"));

        return Arrays.asList(new Item[]{window, window});
    }

}