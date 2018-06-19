package com.financeactive.gatobatch.core.security;

import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.Clock;
import java.time.Instant;

@Configuration
public class SecurityConfig {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Bean
    AuthAPI authAPI() {
        return new AuthAPI(domain, clientId, clientSecret);
    }

    @Bean
    Converter<TokenHolder, AccessToken> tokenHolder2AccessTokenConverter(Clock clock) {
        return source -> new AccessToken(
                source.getTokenType(),
                source.getAccessToken(),
                Instant.now(clock).plusMillis(source.getExpiresIn())
        );
    }
}
