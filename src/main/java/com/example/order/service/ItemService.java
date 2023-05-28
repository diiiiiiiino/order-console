package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Item;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface ItemService {
    void create(List<String[]> results);
    void decreaseQuantity(List<OrderDto> orders);
    void checkExistsItemNo(List<String> itemNoList);
    void display();
    ConcurrentMap<String, Item> getInventory();
}
