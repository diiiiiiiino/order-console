package com.example.order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {
    String itemNo;
    Long quantity;

    public static OrderDto of(String itemNo, Long quantity){
        return new OrderDto(itemNo, quantity);
    }
}
