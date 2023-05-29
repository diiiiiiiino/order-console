package com.nos.order.service;

import com.nos.order.base.BaseTest;
import com.nos.order.dto.OrderDto;
import com.nos.order.entity.Item;
import com.nos.order.exception.SoldOutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ItemServiceTest extends BaseTest {
    ConcurrentMap<String, Item> inventory;
    final ItemService itemService;

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

    @ParameterizedTest
    @MethodSource("com.nos.order.util.MethodSources#listValidSource")
    @DisplayName("1-3. 아이템 생성 (상품데이터가 null 또는 empty일때)")
    void creteItem1_3(final List<String[]> results) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemService.create(results));
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

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                itemService.decreaseQuantity(orders);
                latch.countDown();
            });
        }

        executorService.shutdown();
        latch.await();

        ConcurrentMap<String, Item> inventory = itemService.getInventory();
        Item item = inventory.get(itemNo);

        Assertions.assertEquals(0, item.getQuantity().get());
    }

    @ParameterizedTest
    @MethodSource("decreaseQuantitySource")
    @DisplayName("2-2. 아이템 수량 감소 (품절 / 재고수량 부족)")
    void decreaseQuantity2_2(final int threadCount, final List<OrderDto> orders) throws InterruptedException {
        final List<String[]> results = List.of(new String[]{"상품번호", "상품명", "판매가격", "재고가격"},
                                               new String[]{"768848", "[STANLEY] GO CERAMIVAC 진공 텀블러/보틀 3종", "21000", "45"});
        final ExceptionHelper exceptionHelper = new ExceptionHelper();

        itemService.create(results);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemService.decreaseQuantity(orders);
                } catch (SoldOutException e){
                    exceptionHelper.setException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        executorService.shutdown();
        latch.await();

        Assertions.assertNotNull(exceptionHelper.getException());
    }

    @ParameterizedTest
    @MethodSource("com.nos.order.util.MethodSources#listValidSource")
    @DisplayName("2-3. 아이템 수량 감소 (요청데이터가 null 또는 empty일때)")
    void decreaseQuantity2_3(final List<OrderDto> orders) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemService.decreaseQuantity(orders));
    }

    @ParameterizedTest
    @MethodSource("checkExistsItemNo3_1Source")
    @DisplayName("3-1. 상품번호 존재여부 확인 (상품 존재)")
    void checkExistsItemNo3_1(final List<String> itemNoList, final List<Item> items) {
        items.stream().forEach(item -> inventory.put(item.getNo(), item));

        Assertions.assertDoesNotThrow(() -> itemService.checkExistsItemNo(itemNoList));
    }

    @ParameterizedTest
    @MethodSource("checkExistsItemNo3_2Source")
    @DisplayName("3-2. 상품번호 존재여부 확인 (상품 존재X)")
    void checkExistsItemNo3_2(final List<String> itemNoList, final List<Item> items) {
        items.stream().forEach(item -> inventory.put(item.getNo(), item));

        Assertions.assertThrows(NoSuchElementException.class, () -> itemService.checkExistsItemNo(itemNoList));
    }

    @ParameterizedTest
    @MethodSource("com.nos.order.util.MethodSources#listValidSource")
    @DisplayName("3-3. 상품번호 존재여부 확인 (요청데이터가 null 또는 empty일때)")
    void checkExistsItemNo3_3(final List<String> itemNoList) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemService.checkExistsItemNo(itemNoList));
    }


    @ParameterizedTest
    @MethodSource("displaySource")
    @DisplayName("4-1. 상품번호 존재여부 확인 (요청데이터가 null일때)")
    void display4_1(final ConcurrentMap<String, Item> pInventory) {
        inventory = pInventory;
        Assertions.assertThrows(NoSuchElementException.class, () -> itemService.display());
    }

    static Stream decreaseQuantitySource(){
        return Stream.of(
                Arguments.of(6, List.of(OrderDto.of("768848", 9L))),
                Arguments.of(5, List.of(OrderDto.of("768848", 10L)))
        );
    }

    static Stream checkExistsItemNo3_1Source(){
        Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .build();

        Item item2 = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(19000))
                .build();

        return Stream.of(
                Arguments.of(List.of("648418"), List.of(item, item2)),
                Arguments.of(List.of("648418", "748943"), List.of(item, item2))
        );
    }

    static Stream checkExistsItemNo3_2Source(){
        Item item = Item.builder()
                .no("648418")
                .name("BS 02-2A DAYPACK 26 (BLACK)")
                .price(BigDecimal.valueOf(238000))
                .build();

        Item item2 = Item.builder()
                .no("748943")
                .name("디오디너리 데일리 세트 (Daily set)")
                .price(BigDecimal.valueOf(19000))
                .build();

        return Stream.of(
                Arguments.of(List.of("779049"), List.of(item, item2)),
                Arguments.of(List.of("768848", "779049"), List.of(item, item2)),
                Arguments.of(List.of("648418", "779049"), List.of(item, item2))
        );
    }

    static Stream displaySource(){
        return Stream.of(
                null,
                new ConcurrentHashMap<>()
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
