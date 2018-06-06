package com.financeactive.gatobatch;

import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
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
    ApplicationRunner runner(AuthAPI authAPI, RestTemplate restTemplate) {
        return args -> {
            TokenHolder holder = authAPI.requestToken("https://gato-api.herokuapp.com/").execute();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", String.format("%s %s", holder.getTokenType(), holder.getAccessToken()));
            ResponseEntity<Object> stuff = restTemplate.exchange("/user", HttpMethod.GET, new HttpEntity<>(headers), Object.class);
            System.out.println(stuff);
        };
    }
}
