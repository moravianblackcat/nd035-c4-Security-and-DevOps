package com.example.demo.controller.base;

import com.example.demo.controller.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.WebSecurityConfiguration;
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
@WebMvcTest(value = UserController.class, useDefaultFilters = false)
@Import(UserController.class)
@ContextConfiguration(classes = WebSecurityConfiguration.class)
public abstract class UserControllerTestBase {

    protected final String CREATE_USER_ENDPOINT = "/api/user/create";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    protected User createUser(String username, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    protected CreateUserRequest createUserRequest(String username) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword("password");
        request.setConfirmPassword("password");

        return request;
    }

}
