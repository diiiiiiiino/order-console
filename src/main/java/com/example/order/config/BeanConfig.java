package com.example.order.config;

import com.example.order.entity.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class BeanConfig {

    @Bean
    public ConcurrentHashMap<String, Item> inventory(){
        return new ConcurrentHashMap<>();
    }
}
