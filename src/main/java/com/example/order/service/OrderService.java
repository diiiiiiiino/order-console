package com.example.order.service;

import com.example.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    void order(List<OrderDto> orders);
}
