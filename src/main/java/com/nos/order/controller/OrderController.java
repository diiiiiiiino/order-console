package com.nos.order.controller;

import com.nos.order.dto.OrderDto;
import com.nos.order.entity.Order;
import com.nos.order.excel.ItemFileReader;
import com.nos.order.service.ItemService;
import com.nos.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderController {
    private final ItemFileReader itemFileReader;
    private final ItemService itemService;
    private final OrderService orderService;

    public void run() {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            itemService.create(itemFileReader.read());

            while(true){
                System.out.println("입력(o[order]: 주문, q[quit]: 종료) : ");
                String input = bufferedReader.readLine();

                if("q".equals(input) || "quit".equals(input)) {
                    System.out.println("고객님의 주문 감사합니다.");
                    break;
                }

                if("o".equals(input)){
                    List<OrderDto> orders = new ArrayList<>();
                    itemService.display();

                    while(true){
                        System.out.println("상품번호 : ");
                        String itemNo = bufferedReader.readLine();

                        System.out.println("수량 : ");
                        String quantityStr = bufferedReader.readLine();

                        if(!StringUtils.hasText(itemNo) || !StringUtils.hasText(quantityStr)){
                            try{
                                Order order = orderService.order(orders);
                                System.out.println(order);
                            } catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            break;
                        } else {
                            try{
                                Long quantity = Long.valueOf(quantityStr);
                                orders.add(OrderDto.of(itemNo, quantity));
                            } catch (NumberFormatException e){
                                System.out.println("수량이 유효한 값이 아닙니다.");
                            }
                        }
                    }
                } else {
                    System.out.println("주문가능한 커맨드가 아닙니다.");
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
