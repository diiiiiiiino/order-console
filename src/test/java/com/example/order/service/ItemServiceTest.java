package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.entity.Item;
import com.example.order.exception.SoldOutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ItemServiceTest {
    ItemService itemService;
    ConcurrentMap<String, Item> inventory;

    public ItemServiceTest() {
        inventory = new ConcurrentHashMap<>();
        itemService = new ItemServiceImpl(inventory);
    }

    @Test
    @DisplayName("1-1. 아이템 생성 (정상)")
    void creteItem1_1() {
        final List<String[]> results = List.of(new String[]{"상품번호", "상품명", "판매가격", "재고가격"},
                                               new String[]{"768848", "[STANLEY] GO CERAMIVAC 진공 텀블러/보틀 3종", "21000", "45"},
                                               new String[]{"748943", "디오디너리 데일리 세트 (Daily set)", "19000", "89"},
                                               new String[]{"779989", "버드와이저 HOME DJing 굿즈 세트", "35000", "43"},
                                               new String[]{"779943", "Fabrik Pottery Flat Cup & Saucer - Mint", "24900", "89"});

        itemService.create(results);

        ConcurrentMap<String, Item> concurrentMap = itemService.getInventory();

        Assertions.assertEquals(results.size() - 1, concurrentMap.size());
    }

    @Test
    @DisplayName("1-2. 아이템 생성 (헤더만 존재할 경우)")
    void creteItem1_2() {
        final List<String[]> results = new ArrayList<>();
        results.add(new String[]{"상품번호", "상품명", "판매가격", "재고가격"});

        itemService.create(results);

        ConcurrentMap<String, Item> concurrentMap = itemService.getInventory();

        Assertions.assertEquals(0, concurrentMap.size());
    }

    @Test
    @DisplayName("1-3. 아이템 생성 (비어있을 경우)")
    void creteItem1_3() {
        final List<String[]> results = new ArrayList<>();

        itemService.create(results);

        ConcurrentMap<String, Item> concurrentMap = itemService.getInventory();

        Assertions.assertEquals(0, concurrentMap.size());
    }

    @Test
    @DisplayName("2-1. 아이템 수량 감소 (정상)")
    void decreaseQuantity2_1() throws InterruptedException {
        final int threadCount = 5;
        final String itemNo = "768848";
        final List orders = List.of(OrderDto.of(itemNo, 9L));

        final List<String[]> results = List.of(new String[]{"상품번호", "상품명", "판매가격", "재고가격"},
                                               new String[]{"768848", "[STANLEY] GO CERAMIVAC 진공 텀블러/보틀 3종", "21000", "45"});

        itemService.create(results);

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> itemService.decreaseQuantity(orders));
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        ConcurrentMap<String, Item> inventory = itemService.getInventory();
        Assertions.assertEquals(0, inventory.get(itemNo).getQuantity());
    }

    @ParameterizedTest
    @MethodSource("decreaseQuantitySource")
    @DisplayName("2-2. 아이템 수량 감소 (품절 / 재고수량 부족)")
    void decreaseQuantity2_2(final int threadCount, final List<OrderDto> orders) throws InterruptedException {
        final List<String[]> results = List.of(new String[]{"상품번호", "상품명", "판매가격", "재고가격"},
                                               new String[]{"768848", "[STANLEY] GO CERAMIVAC 진공 텀블러/보틀 3종", "21000", "45"});
        final ExceptionHelper exceptionHelper = new ExceptionHelper();

        itemService.create(results);

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    itemService.decreaseQuantity(orders);
                } catch (SoldOutException e){
                    exceptionHelper.setException(e);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        Assertions.assertNotNull(exceptionHelper.getException());
    }

    static Stream decreaseQuantitySource(){
        return Stream.of(
                Arguments.of(6, List.of(OrderDto.of("768848", 9L))),
                Arguments.of(5, List.of(OrderDto.of("768848", 10L)))
        );
    }

    static class ExceptionHelper {
        private Exception exception;

        public synchronized void setException(Exception e) {
            if (exception == null) {
                exception = e;
            }
        }

        public synchronized Exception getException() {
            return exception;
        }
    }
}
