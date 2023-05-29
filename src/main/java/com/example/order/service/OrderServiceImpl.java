package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Order;
import com.example.order.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ItemService itemService;
    private final OrderDetailService orderDetailService;
    private final PriceStrategy shipPriceStrategy;

    @Override
    public Order order(List<OrderDto> orders) {
        if(orders == null || orders.isEmpty()){
            throw new IllegalArgumentException("요청한 주문 내역이 없습니다.");
        }

        List<String> itemNoList = orders.stream()
                .map(OrderDto::getItemNo)
                .collect(Collectors.toList());

        itemService.checkExistsItemNo(itemNoList);
        itemService.decreaseQuantity(orders);

        List<OrderDetail> details = orderDetailService.create(orders);

        BigDecimal orderPrice = details.stream()
                .map(OrderDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shipPrice = shipPriceStrategy.calculate(details);

        Order order = Order.builder()
                .orderPrice(orderPrice)
                .shipPrice(shipPrice)
                .payPrice(orderPrice.add(shipPrice))
                .details(details)
                .build();

        return order;
    }
}
