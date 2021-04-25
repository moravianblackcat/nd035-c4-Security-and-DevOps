package com.example.demo.service;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.service.exception.ItemWithThisIdWasNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService cut = new ItemServiceImpl();

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

        assertThrows(ItemWithThisIdWasNotFoundException.class, () -> cut.findById(1L));
    }

    private Item createItem() {
        Item item = new Item();
        item.setDescription("Test Item");
        item.setName("Item");
        item.setPrice(new BigDecimal("12.50"));

        return item;
    }

}