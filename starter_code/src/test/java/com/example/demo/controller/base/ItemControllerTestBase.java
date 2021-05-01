package com.example.demo.controller.base;

import com.example.demo.controller.ItemController;
import com.example.demo.security.WebSecurityConfiguration;
import com.example.demo.service.ItemService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ItemController.class, useDefaultFilters = false)
@Import(ItemController.class)
@ContextConfiguration(classes = WebSecurityConfiguration.class)
public class ItemControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ItemService itemService;

}
