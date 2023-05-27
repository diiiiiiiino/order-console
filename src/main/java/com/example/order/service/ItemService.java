package com.example.order.service;

import com.example.order.dto.ItemDto;
import com.example.order.entity.Item;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface ItemService {
    List<ItemDto> create(List<String[]> results) throws IOException;
    void print(List<ItemDto> items);
    void decreaseQuantity(String itemNo, Long quantity);
    ConcurrentMap<String, Item> getInventory();
}
