package com.example.order.service;

import com.example.order.dto.ItemDto;
import com.example.order.entity.Item;
import com.example.order.enumeration.SoldOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ConcurrentMap<String, Item> inventory;

    @Override
    public List<ItemDto> create(List<String[]> results) {
        List<ItemDto> itemDtos = new ArrayList<>();

        if(results.isEmpty()) return itemDtos;

        List<Item> items = results.subList(1, results.size()).stream()
                .map(Item::of)
                .collect(Collectors.toList());

        ConcurrentMap<String, Item> concurrentMap = items.stream()
                .collect(Collectors.toConcurrentMap(Item::getNo, item -> item));

        inventory.putAll(concurrentMap);

        itemDtos = items.stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());

        return itemDtos;
    }

    @Override
    public ConcurrentMap<String, Item> getInventory() {
        return inventory;
    }

    @Override
    public synchronized void decreaseQuantity(String itemNo, Long orderQuantity) {
        Item item = inventory.get(itemNo);
        String itemName = item.getName();
        Long itemQuantity = item.getQuantity();

        if(itemQuantity == 0){
            StringBuffer sb = new StringBuffer();
            sb.append(itemName)
              .append("의 재고가 부족합니다.");

            throw new SoldOutException(sb.toString());
        } else if(itemQuantity - orderQuantity < 0){
            StringBuffer sb = new StringBuffer();
            sb.append(itemName)
                    .append("의 재고가")
                    .append(itemQuantity - orderQuantity)
                    .append("개가 부족합니다.");
        }

        item.setQuantity(itemQuantity - orderQuantity);

        inventory.put(itemNo, item);
    }

    @Override
    public void print(List<ItemDto> items){
        StringBuffer sb = new StringBuffer("상품번호\t\t\t\t상품명\t\t\t\t\t\t\t\t\t\t\t\t판매가격\t\t\t\t재고수\n");

        String itemStr = inventory.entrySet().stream()
                .map(stringItemEntry -> stringItemEntry.getValue().toString())
                .collect(Collectors.joining("\n"));

        sb.append(itemStr);

        System.out.println(sb);
    }
}
