package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cut = new CartServiceImpl();

    private Cart cart;
    private Item item;
    private BigDecimal price = new BigDecimal(2.99);
    private int quantity;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        item = new Item();
        item.setPrice(price);
        quantity = 2;
    }

    @Test
    public void addItemsAndSaveAddsItems() {
        cut.addItemsAndSave(cart, item, quantity);

        assertIterableEquals(Arrays.asList(item, item), cart.getItems());
    }

    @Test
    public void addItemsAndSaveAddsPrices() {
        cart.setTotal(new BigDecimal(0.0));
        cut.addItemsAndSave(cart, item, quantity);

        assertEquals(0, cart.getTotal().compareTo(new BigDecimal(5.98)));
    }

    @Test
    public void addItemsAndSaveSaves() {
        cut.addItemsAndSave(cart, item, quantity);

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void removeItemsAndSaveRemovesItems() {
        cart.addItem(item);
        cart.addItem(item);
        cut.removeItemsAndSave(cart, item, quantity);

        assertIterableEquals(Arrays.asList(), cart.getItems());
    }

    @Test
    public void removeItemsAndSaveRemovesPrices() {
        cart.setTotal(new BigDecimal(5.98));
        cut.removeItemsAndSave(cart, item, quantity);

        assertEquals(0, cart.getTotal().compareTo(new BigDecimal(0.0)));
    }

    @Test
    public void removeItemsAndSaveSaves() {
        cut.removeItemsAndSave(cart, item, quantity);

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void saveCartSavesCart() {
        Cart cart = new Cart();

        cut.saveCart(cart);

        verify(cartRepository, times(1)).save(cart);
    }

}