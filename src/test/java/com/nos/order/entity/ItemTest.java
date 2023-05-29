package com.nos.order.entity;

import com.nos.order.base.BaseTest;
import com.nos.order.exception.SoldOutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class ItemTest extends BaseTest {

    @ParameterizedTest
    @MethodSource("decreaseQuantity1_1Source")
    @DisplayName("1-1. 수량 감소 (정상)")
    void decreaseQuantity(long quantity, long orderQuantity){
        final Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .quantity(new AtomicLong(quantity))
                .build();

        item.decreaseQuantity(orderQuantity);

        Assertions.assertEquals(quantity - orderQuantity, item.getQuantity().get());
    }

    @ParameterizedTest
    @MethodSource("decreaseQuantity1_2Source")
    @DisplayName("1-2. 아이템 수량 감소 (품절 / 재고수량 부족)")
    void decreaseQuantity1_2(long quantity, long orderQuantity){
        final Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .quantity(new AtomicLong(quantity))
                .build();

        Assertions.assertThrows(SoldOutException.class, () -> item.decreaseQuantity(orderQuantity));
    }

    static Stream decreaseQuantity1_1Source(){
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(3, 2)
        );
    }

    static Stream decreaseQuantity1_2Source(){
        return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(1, 2)
        );
    }
}
