package com.financeactive.gatobatch.core;

import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.List;

@Configuration
public class CoreConfig {

    @Value("${gato.api.endpoint}")
    private String apiEndpoint;

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors) {
        return new RestTemplateBuilder().rootUri(apiEndpoint).interceptors(interceptors).build();
    }

    @Bean
    ClientHttpRequestInterceptor authorizationHeaderInterceptor(@Value("${auth0.audience}") String auth0Audience, AuthAPI authAPI) {
        return (request, body, execution) -> {
            TokenHolder holder = authAPI.requestToken(auth0Audience).execute();
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, String.format("%s %s", holder.getTokenType(), holder.getAccessToken()));
            return execution.execute(request, body);
        };
    }
}
