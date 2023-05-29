package com.nos.order.service;

import com.nos.order.dto.OrderDto;
import com.nos.order.entity.Item;
import com.nos.order.entity.Order;
import com.nos.order.entity.OrderDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    final ItemService itemService;
    final PriceStrategy priceStrategy;
    final OrderDetailService orderDetailService;
    final OrderService orderService;

    public OrderServiceTest() {
        itemService = mock(ItemService.class);
        orderDetailService = mock(OrderDetailService.class);
        priceStrategy = mock(ShipPriceStrategy.class);

        orderService = new OrderServiceImpl(itemService, orderDetailService, priceStrategy);
    }

    @Test
    @DisplayName("1-1. 주문 (정상)")
    void order1_1() {
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

        final List<OrderDto> orders = List.of(OrderDto.of("648418", 1L),
                                        OrderDto.of("748943", 2L));

        final List<OrderDetail> details = List.of(OrderDetail.of(item, 1L),
                                            OrderDetail.of(item2, 2L));

        when(orderDetailService.create(anyList())).thenReturn(details);
        when(priceStrategy.calculate(anyList())).thenReturn(BigDecimal.valueOf(2500));

        final Order order = orderService.order(orders);

        Assertions.assertEquals(BigDecimal.valueOf(276_000), order.getOrderPrice());
        Assertions.assertEquals(BigDecimal.valueOf(2500), order.getShipPrice());
        Assertions.assertEquals(BigDecimal.valueOf(276_000 + 2500), order.getPayPrice());
        Assertions.assertEquals(orders.size(), order.getDetails().size());

        verify(itemService, times(1)).checkExistsItemNo(anyList());
        verify(itemService, times(1)).decreaseQuantity(anyList());
        verify(orderDetailService, times(1)).create(anyList());
        verify(priceStrategy, times(1)).calculate(anyList());
    }

    @ParameterizedTest
    @MethodSource("com.example.order.util.MethodSources#listValidSource")
    @DisplayName("1-2. 주문 (주문내역이 null 또는 empty일때)")
    void order1_2(List<OrderDto> orders) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderService.order(orders));

        verify(itemService, times(0)).checkExistsItemNo(anyList());
        verify(itemService, times(0)).decreaseQuantity(anyList());
        verify(orderDetailService, times(0)).create(anyList());
        verify(priceStrategy, times(0)).calculate(anyList());
    }

    @Test
    @DisplayName("1-3. 주문 (주문내역 중 존재하지 않는 상품일때)")
    void order1_3() {
        doThrow(new IllegalArgumentException())
                .when(itemService).checkExistsItemNo(anyList());

        final List<OrderDto> details = List.of(OrderDto.of("648418", 2L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderService.order(details));

        verify(itemService, times(1)).checkExistsItemNo(anyList());
        verify(itemService, times(0)).decreaseQuantity(anyList());
        verify(orderDetailService, times(0)).create(anyList());
        verify(priceStrategy, times(0)).calculate(anyList());
    }
}
