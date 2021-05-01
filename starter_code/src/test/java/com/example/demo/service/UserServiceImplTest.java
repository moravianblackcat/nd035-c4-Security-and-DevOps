package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.service.exception.InvalidPasswordException;
import com.example.demo.service.exception.UserWithThisIdWasNotFoundException;
import com.example.demo.service.exception.UserWithThisUsernameAlreadyExistsException;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import com.example.demo.service.strategy.PasswordCreationStrategy;
import com.example.demo.service.strategy.PasswordValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordValidationStrategy passwordValidationStrategy;

    @Mock
    private PasswordCreationStrategy passwordCreationStrategy;

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
    public void createNewUserCreatesNewUserWithEmptyCart() {
        String username = "username";

        Cart empty = new Cart();
        when(cartRepository.save(any(Cart.class))).thenReturn(empty);

        User user = new User();
        user.setUsername(username);
        user.setCart(empty);
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(passwordValidationStrategy.isValid(anyString(), anyString())).thenReturn(true);

        User newUser = cut.createNewUser(username, "password", "password");

        assertEquals(username, newUser.getUsername());
        assertTrue(newUser.hasEmptyCart());
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
    public void getUserCartByUsernameReturnsUsersCartIfUserExists() {
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        assertEquals(cart,
                cut.getUserCartByUsername(user.getUsername()),
                "Get user cart by username operation didn't return user's cart.");
    }

    @Test
    public void getUserCartByUsernameThrowsAnErrorIfUserDoesNotExist() {
        when(userRepository.findByUsername(anyString())).thenThrow(new UserWithThisUsernameWasNotFoundException());

        assertThrows(UserWithThisUsernameWasNotFoundException.class,
                () -> cut.getUserCartByUsername("username"),
                "Get user cart by username operation didn't throw an error, although an user doesn't exist.");
    }

    @Test
    public void creationOfNotAlreadyExistingUserSaves() {
        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
        when(passwordValidationStrategy.isValid(anyString(), anyString())).thenReturn(true);

        cut.createNewUser("username", "password", "password");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void creationOfAlreadyExistingUserThrowsAnError() {
        when(userRepository.existsUserByUsername((user.getUsername()))).thenReturn(true);
        when(passwordValidationStrategy.isValid(anyString(), anyString())).thenReturn(true);

        assertThrows(UserWithThisUsernameAlreadyExistsException.class,
                () -> cut.createNewUser(user.getUsername(), "password", "password"),
                "Save user operation didn't throw an error, although given user already exists.");
    }

    @Test
    public void creationWithValidPasswordSucceeds() {
        when(passwordValidationStrategy.isValid(anyString(), anyString())).thenReturn(true);

        cut.createNewUser("New User", "password", "password");
    }

    @Test
    public void creationWithInvalidPasswordFails() {
        when(passwordValidationStrategy.isValid(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> cut.createNewUser("username", "invalid", "invalid"),
                "Save user operation didn't throw an error, although given password doesn't meet the criteria.");
    }
}