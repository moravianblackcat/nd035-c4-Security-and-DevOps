package com.example.demo.controller;

import com.example.demo.controller.base.UserControllerTestBase;
import com.example.demo.model.persistence.User;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
public class UserControllerAuthenticatedUserTest extends UserControllerTestBase {

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

}