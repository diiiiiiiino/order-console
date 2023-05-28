package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Order;

import java.util.List;

public interface OrderService {
    Order order(List<OrderDto> orders);
}
