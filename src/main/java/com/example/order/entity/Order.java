package com.example.order.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Builder
@Getter
public class Order {
    BigDecimal orderPrice;
    BigDecimal shipPrice;
    BigDecimal payPrice;

    List<OrderDetail> details;

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###원");
        final String divineLine = "--------------------------------\n";

        StringBuilder sb = new StringBuilder();
        sb.append(divineLine);

        for(OrderDetail detail : details){
            sb.append(detail);
        }
        sb.append(divineLine);
        sb.append("주문금액 : ").append(decimalFormat.format(orderPrice)).append("\n");

        if(shipPrice.compareTo(BigDecimal.ZERO) > 0){
            sb.append("배송금액 : ").append(decimalFormat.format(shipPrice)).append("\n");
        }

        sb.append(divineLine);
        sb.append("지불금액 : ").append(decimalFormat.format(payPrice)).append("\n");
        sb.append(divineLine);

        return sb.toString();
    }
}
