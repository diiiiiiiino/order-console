package com.example.order.controller;

import com.example.order.dto.ItemDto;
import com.example.order.dto.OrderDto;
import com.example.order.excel.ItemFileReader;
import com.example.order.service.ItemService;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainController {
    private final ItemFileReader itemFileReader;
    private final ItemService itemService;
    private final OrderService orderService;

    public void run() {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            List<ItemDto> items = itemService.create(itemFileReader.read());

            while(true){
                System.out.println("입력(o[order]: 주문, q[quit]: 종료) : ");
                String input = bufferedReader.readLine();

                if("q".equals(input) || "quit".equals(input)) {
                    System.out.println("주문이 종료되었습니다!");
                    break;
                }

                if("o".equals(input)){
                    itemService.print(items);

                    List<OrderDto> orders = new ArrayList<>();

                    while(true){
                        System.out.println("상품번호 : ");
                        String itemNo = bufferedReader.readLine();

                        System.out.println("수량 : ");
                        String quantityStr = bufferedReader.readLine();

                        if(!StringUtils.hasText(itemNo) || !StringUtils.hasText(quantityStr)){
                            orderService.order(orders);

                            System.out.println("주문 내역");
                            System.out.println("------------------------------");
                            System.out.println("주문금액 : 2,000원");
                            System.out.println("배송비 : 2,500원");
                            System.out.println("------------------------------");
                            System.out.println("지불금액 : 4,500원");
                            System.out.println("------------------------------");
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
            e.printStackTrace();
        }
    }
}
