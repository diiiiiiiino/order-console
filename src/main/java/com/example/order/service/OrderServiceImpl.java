package com.example.order.service;

import com.example.order.dto.ItemDto;
import com.example.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ItemService itemService;

    @Override
    @Transactional
    public void order(List<OrderDto> orders) {
        //1. 상품 조회 및 재고 확인
        List<String> numbers = orders.stream()
                .map(OrderDto::getItemNo)
                .collect(Collectors.toList());

        Set<ItemDto> items = itemService.findAll(numbers);
        List<String> notContainItems = orders.stream()
                .filter(orderDto -> !items.contains(orderDto.getItemNo()))
                .map(OrderDto::getItemNo)
                .collect(Collectors.toList());

        if(notContainItems.size() > 0){
            String notContainItemNo = notContainItems.stream()
                    .collect(Collectors.joining(", "));

            StringBuffer sb = new StringBuffer();
            sb.append("상품번호 : ")
              .append(notContainItemNo)
              .append("의 상품이 존재하지 않습니다.");

            throw new NoSuchElementException(sb.toString());
        }

        //2. 재고 차감


        //3. 주문 내역 생성 및 리턴
    }
}
