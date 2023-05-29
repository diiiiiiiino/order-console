package com.nos.order.config;

import com.nos.order.entity.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class BeanConfig {

    @Bean
    public ConcurrentMap<String, Item> inventory(){
        return new ConcurrentHashMap<>();
    }
}
