package com.nos.order.strategy;

import com.nos.order.entity.Item;
import com.nos.order.entity.OrderDetail;
import com.nos.order.service.PriceStrategy;
import com.nos.order.service.ShipPriceStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class PriceStrategyTest {
    PriceStrategy priceStrategy;

    public PriceStrategyTest() {
        this.priceStrategy = new ShipPriceStrategy();
    }

    @Test
    @DisplayName("1-1. 배송비 정산 (50,000 미만일때)")
    void calculate1_1() {
        final Item item = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(49_999))
                .build();

        List<OrderDetail> details = List.of(OrderDetail.of(item, 1L));

        BigDecimal shipPrice = priceStrategy.calculate(details);

        Assertions.assertEquals(BigDecimal.ZERO, shipPrice);
    }

    @Test
    @DisplayName("1-2. 배송비 정산 (50,000 이상일때)")
    void calculate1_2() {
        final Item item = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(50_001))
                .build();

        List<OrderDetail> details = List.of(OrderDetail.of(item, 1L));

        BigDecimal shipPrice = priceStrategy.calculate(details);

        Assertions.assertEquals(BigDecimal.valueOf(2500), shipPrice);
    }
}
