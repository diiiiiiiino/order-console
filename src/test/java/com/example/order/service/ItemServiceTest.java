package com.example.order.service;

import com.example.order.dto.ItemDto;
import com.example.order.entity.Item;
import com.example.order.excel.CsvItemFileReader;
import com.example.order.excel.ItemFileReader;
import com.example.order.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    ItemService itemService;
    ItemRepository itemRepository;
    ConcurrentHashMap<String, Item> inventory;

    public ItemServiceTest() {
        itemRepository = mock(ItemRepository.class);
        inventory = mock(ConcurrentHashMap.class);
        itemService = new ItemServiceImpl(itemRepository, inventory);
    }

    @Test
    @DisplayName("1-1. 아이템 생성 (정상)")
    void creteItem1_1() throws IOException {
        List<String[]> results = List.of(new String[]{"상품번호", "상품명", "판매가격", "재고가격"},
                                         new String[]{"768848", "[STANLEY] GO CERAMIVAC 진공 텀블러/보틀 3종", "21000", "45"},
                                         new String[]{"748943", "디오디너리 데일리 세트 (Daily set)", "19000", "89"},
                                         new String[]{"779989", "버드와이저 HOME DJing 굿즈 세트", "35000", "43"},
                                         new String[]{"779943", "Fabrik Pottery Flat Cup & Saucer - Mint", "24900", "89"});
        List<Item> items = results.subList(1, results.size()).stream()
                .map(Item::of)
                .collect(Collectors.toList());

        when(itemRepository.saveAll(any(List.class))).thenReturn(items);

        List<ItemDto> itemDtos = itemService.create(results);

        Assertions.assertEquals(results.size() - 1, itemDtos.size());
    }

    @Test
    @DisplayName("1-2. 아이템 생성 (헤더만 존재할 경우)")
    void creteItem1_2() throws IOException {
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"상품번호", "상품명", "판매가격", "재고가격"});

        List<ItemDto> items = itemService.create(results);

        Assertions.assertEquals(0, items.size());
    }

    @Test
    @DisplayName("1-3. 아이템 생성 (비어있을 경우)")
    void creteItem1_3() throws IOException {
        List<String[]> results = new ArrayList<>();

        List<ItemDto> items = itemService.create(results);

        Assertions.assertEquals(0, items.size());
    }
}
