package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> create(List<OrderDto> orders);
}
