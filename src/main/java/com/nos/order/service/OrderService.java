package com.nos.order.service;

import com.nos.order.dto.OrderDto;
import com.nos.order.entity.Order;

import java.util.List;

public interface OrderService {
    Order order(List<OrderDto> orders);
}
