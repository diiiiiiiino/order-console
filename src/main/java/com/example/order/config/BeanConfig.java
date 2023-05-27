package com.example.order.config;

import com.example.order.entity.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.concurrent.ConcurrentMap;

@Configuration
public class BeanConfig {

    @Bean
    public ConcurrentMap<String, Item> inventory(){
        return new ConcurrentReferenceHashMap<>();
    }
}
