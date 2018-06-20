package com.financeactive.gatobatch;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner runner(RestTemplate restTemplate) {
        Runnable ping = () -> {
            ResponseEntity<Object> stuff = restTemplate.getForEntity("/user", Object.class);
            System.out.println(stuff);
        };
        return args -> IntStream.range(0, 5).forEach(i -> ping.run());
    }
}
