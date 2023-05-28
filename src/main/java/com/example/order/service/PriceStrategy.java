package com.example.order.service;

import com.example.order.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface PriceStrategy {
    BigDecimal calculate(List<OrderDetail> items);
}
