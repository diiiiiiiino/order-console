package com.example.order.service;

import com.example.order.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ShipPriceStrategy implements PriceStrategy {

    @Override
    public BigDecimal calculate(List<OrderDetail> details) {
        final int intStandardPrice = 50000;
        final int intShipPrice = 2500;
        final BigDecimal standardPrice = BigDecimal.valueOf(intStandardPrice);
        BigDecimal shipPrice = BigDecimal.ZERO;

        BigDecimal price = details.stream()
                .map(detail -> detail.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(price.compareTo(standardPrice) > 0){
            shipPrice = BigDecimal.valueOf(intShipPrice);
        }

        return shipPrice;
    }
}
