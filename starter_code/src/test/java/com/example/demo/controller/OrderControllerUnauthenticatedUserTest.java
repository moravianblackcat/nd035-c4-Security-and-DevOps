package com.example.demo.controller;

import com.example.demo.controller.base.OrderControllerTestBase;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerUnauthenticatedUserTest extends OrderControllerTestBase {

    @Test
    public void unauthenticatedUserCannotSubmitOrder() throws Exception {
        mockMvc.perform(get("/api/order/submit/myself"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthenticatedUserCannotViewOrderHistory() throws Exception {
        mockMvc.perform(get("/api/order/history/myself"))
                .andExpect(status().isForbidden());
    }


}
