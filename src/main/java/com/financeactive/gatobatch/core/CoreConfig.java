package com.financeactive.gatobatch.core;

import com.financeactive.gatobatch.core.security.AccessToken;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.List;
import java.util.function.Supplier;

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
    ClientHttpRequestInterceptor authorizationHeaderInterceptor(Supplier<AccessToken> accessTokenSupplier) {
        return (request, body, execution) -> {
            AccessToken holder = accessTokenSupplier.get();
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, String.format("%s %s", holder.getType(), holder.getValue()));
            return execution.execute(request, body);
        };
    }

    @Bean
    ClientHttpRequestInterceptor responseInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            LoggerFactory.getLogger(ClientHttpResponse.class)
                    .info("Response status: {}, text: {}, headers: {}", response.getStatusCode(), response.getStatusText(), response.getHeaders());
            return response;
        };
    }
}
