package com.nos.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
