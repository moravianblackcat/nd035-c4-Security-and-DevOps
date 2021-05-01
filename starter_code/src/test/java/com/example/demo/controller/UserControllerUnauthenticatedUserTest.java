package com.example.demo.controller;

import com.example.demo.controller.base.UserControllerTestBase;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.service.exception.InvalidPasswordException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerUnauthenticatedUserTest extends UserControllerTestBase {

    @Test
    public void postNonExistingUserWithValidPasswordCreatesAnUser() throws Exception {
        String username = "Test User";

        CreateUserRequest request = createUserRequest(username);

        User toBeCreated = new User();
        toBeCreated.setUsername(username);

        User created = createUser(username, 1L);

        when(userService.createNewUser(anyString(), anyString(), anyString())).thenReturn(created);

        mockMvc.perform(post(CREATE_USER_ENDPOINT)
                .with(csrf())
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

        when(userService.createNewUser(anyString(), anyString(), anyString())).thenThrow(new UserWithThisUsernameAlreadyExistsException());

        mockMvc.perform(post(CREATE_USER_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("User with this username already exists."));
    }

    @Test
    public void postUserWithInvalidPasswordSucceeds() throws Exception {
        when(userService.createNewUser(anyString(), anyString(), anyString())).thenThrow(new InvalidPasswordException());

        mockMvc.perform(post(CREATE_USER_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest("username"))))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Your password doesn't comply with our rules."));
    }

    @Test
    public void unauthenticatedUserCannotGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/id/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthenticatedUserCannotGetUserByName() throws Exception {
        mockMvc.perform(get("/api/user/username"))
                .andExpect(status().isForbidden());
    }

}
