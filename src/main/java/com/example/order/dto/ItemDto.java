package com.example.order.dto;

import com.example.order.entity.Item;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;

@Builder
public class ItemDto {
    String no;
    String name;
    BigDecimal price;
    Long quantity;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(no).append("\t\t\t")
                .append(name).append("\t\t\t")
                .append(price).append("\t\t\t")
                .append(quantity).append("\t\t\t")
                .toString();
    }

    public static ItemDto from(Item item){
        return ItemDto.builder()
                .no(item.getNo())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}
