package com.example.demo.service;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.service.exception.ItemWithThisIdWasNotFoundException;
import com.example.demo.service.exception.ItemWithThisNameWasNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(ItemWithThisIdWasNotFoundException::new);
    }

    @Override
    public List<Item> findByName(String name) {
        List<Item> result = itemRepository.findByName(name);
        if (result.isEmpty()) {
            throw new ItemWithThisNameWasNotFoundException();
        }

        return result;
    }
}
