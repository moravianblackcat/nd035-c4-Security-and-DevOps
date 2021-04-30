package com.example.demo.service;

import com.example.demo.model.persistence.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id);

    List<Item> findByName(String name);

}
