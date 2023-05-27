package com.example.order.service;

import com.example.order.dto.ItemDto;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ItemService {
    List<ItemDto> create(List<String[]> results) throws IOException;
    void print(List<ItemDto> items);
    Set<ItemDto> findAll(List<String> numbers);
    void decreaseQuantity(String itemNo);
}
