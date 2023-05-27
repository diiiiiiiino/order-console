package com.example.order.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
public class Item {
    Long id;
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

    public static Item of(String[] itemRow){
        Objects.requireNonNull(itemRow);

        if(itemRow.length != 4){
            return Item.builder()
                    .no("-----")
                    .name("-----")
                    .price(new BigDecimal("0"))
                    .quantity(0L)
                    .build();
        } else {
            return Item.builder()
                    .no(itemRow[0])
                    .name(itemRow[1].replace("\"", ""))
                    .price(new BigDecimal(itemRow[2]))
                    .quantity(Long.valueOf(itemRow[3]))
                    .build();
        }
    }
}
