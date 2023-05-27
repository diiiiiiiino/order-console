package com.example.order.service;

import com.example.order.dto.ItemDto;
import com.example.order.entity.Item;
import com.example.order.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ConcurrentHashMap<String, Item> inventory;

    @Override
    @Transactional
    public List<ItemDto> create(List<String[]> results) {
        List<ItemDto> itemDtos = new ArrayList<>();

        if(results.isEmpty()) return itemDtos;

        List<Item> items = results.subList(1, results.size()).stream()
                .map(Item::of)
                .collect(Collectors.toList());
        items = itemRepository.saveAll(items);

        itemDtos = items.stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());

        return itemDtos;
    }

    @Override
    public void decreaseQuantity(String itemNo) {

    }

    @Override
    public Set<ItemDto> findAll(List<String> numbers) {
        Set<ItemDto> set = itemRepository.findAllByNoIn(numbers)
                .stream()
                .map(ItemDto::from)
                .collect(Collectors.toSet());
        return set;
    }

    @Override
    public void print(List<ItemDto> items){
        StringBuffer sb = new StringBuffer("상품번호\t\t\t\t상품명\t\t\t\t\t\t\t\t\t\t\t\t판매가격\t\t\t\t재고수\n");

        String itemStr = items.stream()
                .map(ItemDto::toString)
                .collect(Collectors.joining("\n"));

        sb.append(itemStr);

        System.out.println(sb);
    }
}
