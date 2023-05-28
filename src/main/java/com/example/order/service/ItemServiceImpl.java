package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ConcurrentMap<String, Item> inventory;

    @Override
    public void create(List<String[]> results) {
        if(results == null || results.isEmpty()) return;

        List<Item> items = results.subList(1, results.size()).stream()
                .map(Item::from)
                .collect(Collectors.toList());

        ConcurrentMap<String, Item> concurrentMap = items.stream()
                .collect(Collectors.toConcurrentMap(Item::getNo, item -> item));

        inventory.putAll(concurrentMap);
    }

    @Override
    public synchronized void decreaseQuantity(List<OrderDto> orders) {
        if(orders == null || orders.isEmpty()) return;

        for(OrderDto order : orders){
            Item item = inventory.get(order.getItemNo());
            item.decreaseQuantity(order.getQuantity());

            inventory.put(order.getItemNo(), item);
        }
    }

    @Override
    public void checkExistsItemNo(List<String> itemNoList){
        if(itemNoList == null || itemNoList.isEmpty()) return;

        List<String> notContainItems = itemNoList.stream()
                .filter(itemNo -> !inventory.containsKey(itemNo))
                .collect(Collectors.toList());

        if(notContainItems.size() > 0){
            String notContainItemNo = notContainItems.stream()
                    .collect(Collectors.joining(", "));

            throw new NoSuchElementException("상품번호 : " + notContainItemNo + "의 상품이 존재하지 않습니다.");
        }
    }

    @Override
    public void display(){
        if(inventory == null || inventory.isEmpty()){
            System.out.println("주문 가능한 상품이 없습니다.");
            return;
        }

        StringBuilder sb = new StringBuilder("상품번호\t\t\t");
        sb.append("상품명").append("\t\t\t")
          .append("판매가격").append("\t\t\t")
          .append("재고수").append("\n");

        String itemStr = inventory.values().stream()
                .map(Item::toString)
                .collect(Collectors.joining("\n"));

        sb.append(itemStr);

        System.out.println(sb);
    }

    @Override
    public ConcurrentMap<String, Item> getInventory() {
        return inventory;
    }
}
