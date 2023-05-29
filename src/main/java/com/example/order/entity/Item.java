package com.example.order.entity;

import com.example.order.exception.SoldOutException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Builder
public class Item {
    String no;
    String name;
    BigDecimal price;
    AtomicLong quantity;

    public void decreaseQuantity(Long orderQuantity){
        if(quantity.get() == 0){
            throw new SoldOutException("SoldOutException 발생. 재고가 부족합니다.");
        } else if(quantity.get() < orderQuantity){
            throw new SoldOutException("SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다");
        }

        quantity.addAndGet(-orderQuantity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(no).append("\t\t\t")
                .append(name).append("\t\t\t\t\t\t")
                .append(price).append("\t\t\t\t\t\t")
                .append(quantity).append("\t\t\t")
                .toString();
    }

    public static Item from(String[] itemRow){
        Objects.requireNonNull(itemRow);

        if(itemRow.length != 4){
            return Item.builder()
                    .no("-----")
                    .name("-----")
                    .price(BigDecimal.ZERO)
                    .quantity(new AtomicLong(0))
                    .build();
        } else {
            return Item.builder()
                    .no(itemRow[0])
                    .name(itemRow[1].replace("\"", ""))
                    .price(new BigDecimal(itemRow[2]))
                    .quantity(new AtomicLong(Long.valueOf(itemRow[3])))
                    .build();
        }
    }
}
