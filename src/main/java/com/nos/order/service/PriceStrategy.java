package com.nos.order.service;

import com.nos.order.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface PriceStrategy {
    BigDecimal calculate(List<OrderDetail> items);
}
