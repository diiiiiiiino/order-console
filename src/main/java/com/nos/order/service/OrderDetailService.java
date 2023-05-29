package com.nos.order.service;

import com.nos.order.dto.OrderDto;
import com.nos.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> create(List<OrderDto> orders);
}
