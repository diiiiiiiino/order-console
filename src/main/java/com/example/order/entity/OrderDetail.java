package com.example.order.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OrderDetail {
    String itemName;
    BigDecimal price;
    Long quantity;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(itemName).append(" - ").append(quantity).append("개\n");

        return sb.toString();
    }

    public static OrderDetail of(Item item, Long quantity){
        return OrderDetail.builder()
                .itemName(item.getName())
                .price(item.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .quantity(quantity)
                .build();
    }
}
