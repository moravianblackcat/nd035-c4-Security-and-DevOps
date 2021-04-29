package com.example.demo.controller;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CartService cartService;

    @Test
    public void getUserByIdReturnsCorrectUserIfExists() throws Exception {
        String username = "username";
        User user = createUser(username, 1L);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/user/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    public void getUserByIdThrowsAnErrorIfUserDoesNotExist() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new UserWithThisIdWasNotFoundException());

        mockMvc.perform(get("/api/user/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this ID was not found."));
    }

    @Test
    public void getUserByUsernameReturnsCorrectUserIfExists() throws Exception {
        String username = "TestUser";
        User user = createUser(username, 4L);

        when(userService.findByUsername(username)).thenReturn(user);

        mockMvc.perform(get("/api/user/TestUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.cart").doesNotExist());
    }

    @Test
    public void getUserByUsernameThrowsAnErrorIfUserDoesNotExist() throws Exception {
        when(userService.findByUsername(anyString())).thenThrow(new UserWithThisUsernameWasNotFoundException());

        mockMvc.perform(get("/api/user/username"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with this username was not found."));
    }

    @Test
    public void postNonExistingUserCreatesAnUser() throws Exception {
        String username = "Test User";

        CreateUserRequest request = createUserRequest(username);

        User toBeCreated = new User();
        toBeCreated.setUsername(username);

        User created = createUser(username, 1L);

        when(userService.createNewUser(anyString())).thenReturn(created);

        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    public void postExistingUserThrowsAnError() throws Exception {
        String username = "Test User";

        CreateUserRequest request = createUserRequest(username);

        when(userService.createNewUser(anyString())).thenThrow(new UserWithThisUsernameAlreadyExistsException());

        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("User with this username already exists."));
    }

    private User createUser(String username, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    private CreateUserRequest createUserRequest(String username) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);

        return request;
    }

}