package com.example.order.repository;

import com.example.order.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    HashSet<Item> findAllByNoIn(List<String> numbers);
}
