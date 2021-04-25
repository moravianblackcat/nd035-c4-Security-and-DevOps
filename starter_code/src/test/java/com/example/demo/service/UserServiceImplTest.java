package com.example.demo.service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService cut = new UserServiceImpl();

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
    }

    @Test
    public void findByIdReturnsCorrectUserIfExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user,
                cut.findById(1L),
                "Find user by id operation didn't return an user, although it exists.");
    }

    @Test
    public void findByIdThrowsAnErrorIfUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserWithThisIdWasNotFoundException.class, () -> cut.findById(1L));
    }

    @Test
    public void findByUsernameReturnsCorrectUserIfExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        assertEquals(user,
                cut.findByUsername(user.getUsername()),
                "Find user by username operation didn't return an user, although it exists.");
    }

    @Test
    public void findByUsernameUserThrowsAnErrorIfUserDoesNotExist() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UserWithThisUsernameWasNotFoundException.class,
                () -> cut.findByUsername("username"),
                "Find user by username operation didn't throw an error, although an user doesn't exist.");
    }

    @Test
    public void saveOfNotAlreadyExistingUserSaves() {
        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);

        cut.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void saveOfAlreadyExistingUserThrowsAnError() {
        when(userRepository.existsUserByUsername((user.getUsername()))).thenReturn(true);

        assertThrows(UserWithThisUsernameAlreadyExistsException.class,
                () -> cut.saveUser(user),
                "Save user operation didn't throw an error, although given user already exists.");
    }

}