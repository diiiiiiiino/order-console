package com.example.order;

import com.example.order.controller.MainController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
public class OrderApplication {

    private final MainController mainController;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(OrderApplication.class, args);
        MainController mainController = applicationContext.getBean(MainController.class);
        mainController.run();
    }

}
