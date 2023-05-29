package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Item;
import com.example.order.entity.OrderDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.*;

public class OrderDetailServiceTest {
    final ItemService itemService;
    final OrderDetailService orderDetailService;

    public OrderDetailServiceTest() {
        this.itemService = mock(ItemService.class);
        this.orderDetailService = new OrderDetailServiceImpl(itemService);
    }

    @DisplayName("1-1. 주문 상세 생성 (정상)")
    @Test
    void create1_1() {
        final Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .build();

        final Item item2 = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(19000))
                .build();

        ConcurrentMap<String, Item> inventory = new ConcurrentHashMap<>();
        inventory.put(item.getNo(), item);
        inventory.put(item2.getNo(), item2);

        final List<OrderDto> orders = List.of(OrderDto.of("648418", 1L),
                                              OrderDto.of("748943", 2L));

        when(itemService.getInventory()).thenReturn(inventory);

        List<OrderDetail> details = orderDetailService.create(orders);

        Assertions.assertEquals(orders.size(), details.size());
    }

    @DisplayName("1-2. 주문 상세 생성 (판매중인 상품이 아닐때)")
    @Test
    void create1_2() {
        final Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .build();

        final Item item2 = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(19000))
                .build();

        ConcurrentMap<String, Item> inventory = new ConcurrentHashMap<>();
        inventory.put(item.getNo(), item);
        inventory.put(item2.getNo(), item2);

        final List<OrderDto> orders = List.of(OrderDto.of("648418", 1L),
                                              OrderDto.of("999999", 1L));

        when(itemService.getInventory()).thenReturn(inventory);

        Assertions.assertThrows(NoSuchElementException.class, () -> orderDetailService.create(orders));
    }

    @ParameterizedTest
    @MethodSource("com.example.order.util.MethodSources#listValidSource")
    @DisplayName("1-3. 주문 상세 생성 (주문내역이 null 또는 empty일때)")
    void create1_3(List<OrderDto> orders) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.create(orders));

        verify(itemService, times(0)).getInventory();
    }
}
