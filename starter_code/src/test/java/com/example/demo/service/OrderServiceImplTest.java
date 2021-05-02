package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.service.exception.UserWithThisUsernameWasNotFoundException;
import com.example.demo.service.exception.YouCannotSubmitAnEmptyCart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService cut = new OrderServiceImpl();

    @Captor
    private ArgumentCaptor<UserOrder> captor;

    @Test
    public void submitCartCreatesAndSavesOrder() {
        Cart cart = new Cart();
        Item item = new Item();
        List<Item> items = Arrays.asList(item);
        cart.setItems(items);
        BigDecimal total = new BigDecimal("20.5");
        cart.setTotal(total);
        User user = new User();
        cart.setUser(user);

        when(orderRepository.save(captor.capture())).thenReturn(any(UserOrder.class));

        cut.submitCart(cart);

        assertThat(captor.getValue().getItems(), equalTo(items));
        assertThat(captor.getValue().getUser(), is(user));
        assertThat(captor.getValue().getTotal(), equalTo(total));
    }

    @Test
    public void submitEmptyCartThrowsAnError() {
        Cart cart = new Cart();

        assertThrows(YouCannotSubmitAnEmptyCart.class, () -> cut.submitCart(cart));
    }

    @Test
    public void orderHistoryReturnsAllUsersOrders() {
        User user = new User();
        UserOrder order1 = new UserOrder();
        order1.setUser(user);
        UserOrder order2 = new UserOrder();
        order2.setUser(user);

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order1, order2));

        List<UserOrder> orders = cut.getOrdersByUsername("username");
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void orderHistoryForNonExistingUserThrowsAnError() {
        when(userService.findByUsername(anyString())).thenThrow(new UserWithThisUsernameWasNotFoundException());

        assertThrows(UserWithThisUsernameWasNotFoundException.class, () -> cut.getOrdersByUsername("Non existing user"));
    }

}