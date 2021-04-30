package com.example.demo.service;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.service.exception.ItemWithThisIdWasNotFoundException;
import com.example.demo.service.exception.ItemWithThisNameWasNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService cut = new ItemServiceImpl();

    @Test
    public void findAllReturnsAllItems() {
        Item item1 = new Item();
        Item item2 = new Item();

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<Item> items = cut.findAll();
        assertEquals(2,
                items.size(),
                "Find all items operation returns incorrect number of items.");
        assertEquals(List.of(item1, item2),
                items,
                "Find all items operation returns incorrect list of items.");
    }

    @Test
    public void findByIdFindsItemIfExists() {
        Item item = createItem();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertEquals(item,
                cut.findById(1L),
                "Find item by id operation didn't return an item, although it exists.");
    }

    @Test
    public void findByIdThrowsAnErrorIfItemDoesNotExist() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemWithThisIdWasNotFoundException.class,
                () -> cut.findById(1L),
                "Find item by id operation didn't throw an error, although no item was found.");
    }

    @Test
    public void findByNameReturnsAllWithTheName() {
        Item test = createItem();
        Item anotherTest = createItem();

        List<Item> items = List.of(test, anotherTest);
        when(itemRepository.findByName(anyString())).thenReturn(items);

        List<Item> returned = cut.findByName("Item");

        assertEquals(2,
                returned.size(),
                "Find items by name operation returns incorrect number of items.");
        assertEquals(items,
                returned,
                "Find items by name returned incorrect list of items.");
    }

    @Test
    public void findByNameThrowsAnErrorIfNoItemWithSuchNameExists() {
        when(itemRepository.findByName(anyString())).thenReturn(List.of());

        assertThrows(ItemWithThisNameWasNotFoundException.class,
                () -> cut.findByName("name"),
                "Find items by name operation didn't throw an error, although no item was found.");
    }

    private Item createItem() {
        Item item = new Item();
        item.setDescription("Test Item");
        item.setName("Item");
        item.setPrice(new BigDecimal("12.50"));

        return item;
    }

}