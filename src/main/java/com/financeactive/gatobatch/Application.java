package com.financeactive.gatobatch;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner runner(RestTemplate restTemplate) {
        return args -> {
            ResponseEntity<Object> stuff = restTemplate.getForEntity("/user", Object.class);
            System.out.println(stuff);
            stuff = restTemplate.getForEntity("/user", Object.class);
            System.out.println(stuff);
        };
    }
}
