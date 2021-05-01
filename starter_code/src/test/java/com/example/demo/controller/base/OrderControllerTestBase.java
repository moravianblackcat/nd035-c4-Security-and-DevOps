package com.example.demo.controller.base;

import com.example.demo.controller.OrderController;
import com.example.demo.security.WebSecurityConfiguration;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OrderController.class, useDefaultFilters = false)
@Import(OrderController.class)
@ContextConfiguration(classes = WebSecurityConfiguration.class)
public abstract class OrderControllerTestBase {

    protected final String SUBMIT_ENDPOINT = "/api/order/submit/ghost";
    protected final String HISTORY_ENDPOINT = "/api/order/history/ghost";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected UserService userService;

}
