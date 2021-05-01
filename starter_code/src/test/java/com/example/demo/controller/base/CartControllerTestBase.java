package com.example.demo.controller.base;

import com.example.demo.controller.CartController;
import com.example.demo.security.WebSecurityConfiguration;
import com.example.demo.service.CartService;
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CartController.class, useDefaultFilters = false)
@Import(CartController.class)
@ContextConfiguration(classes = WebSecurityConfiguration.class)
public class CartControllerTestBase {

    protected final String ADD_ITEM_ENDPOINT = "/api/cart/addToCart";
    protected final String REMOVE_ITEM_ENDPOINT = "/api/cart/removeFromCart";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected CartService cartService;

    @MockBean
    protected ItemService itemService;

    @MockBean
    protected UserService userService;
    
}
