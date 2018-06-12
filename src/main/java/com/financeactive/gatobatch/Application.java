package com.financeactive.gatobatch;

import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner runner(
            @Value("${gato.api.endpoint}") String gatoApiEndpoint,
            AuthAPI authAPI,
            RestTemplate restTemplate
    ) {
        return args -> {
            TokenHolder holder = authAPI.requestToken(gatoApiEndpoint).execute();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, String.format("%s %s", holder.getTokenType(), holder.getAccessToken()));
            ResponseEntity<Object> stuff = restTemplate.exchange("/user", HttpMethod.GET, new HttpEntity<>(headers), Object.class);
            System.out.println(stuff);
        };
    }
}
