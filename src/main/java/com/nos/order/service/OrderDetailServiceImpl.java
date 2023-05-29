package com.nos.order.service;

import com.nos.order.dto.OrderDto;
import com.nos.order.entity.Item;
import com.nos.order.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{
    private final ItemService itemService;

    @Override
    public List<OrderDetail> create(List<OrderDto> orders) {
        if(orders == null || orders.isEmpty())
            throw new IllegalArgumentException("주문가능한 요청내역이 없습니다.");

        ConcurrentMap<String, Item> inventory = itemService.getInventory();

        return orders.stream()
                .map(order -> {
                    Item item = inventory.get(order.getItemNo());
                    if(item == null){
                        throw new NoSuchElementException("판매중인 상품이 아닙니다. [상품번호 : " + order.getItemNo() + "]");
                    }
                    return OrderDetail.of(item, order.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
