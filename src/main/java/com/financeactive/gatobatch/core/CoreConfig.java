package com.financeactive.gatobatch.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
public class CoreConfig {

    @Value("${gato.api.endpoint}")
    private String apiEndpoint;

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri(apiEndpoint).build();
    }
}
