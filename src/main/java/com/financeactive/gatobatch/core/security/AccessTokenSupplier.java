package com.financeactive.gatobatch.core.security;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.synchronizedMap;

@Component
public class AccessTokenSupplier implements Supplier<AccessToken> {

    private final String auth0Audience;

    private final AuthAPI authAPI;

    private final Converter<TokenHolder, AccessToken> converter;

    private final Map<Class<AccessToken>, AccessToken> cache;

    public AccessTokenSupplier(@Value("${auth0.audience}") String auth0Audience, AuthAPI authAPI, Converter<TokenHolder, AccessToken> converter) {
        this.auth0Audience = auth0Audience;
        this.authAPI = authAPI;
        this.converter = converter;
        this.cache = synchronizedMap(new PassiveExpiringMap<>((PassiveExpiringMap.ExpirationPolicy<Class<AccessToken>, AccessToken>) (key, value) -> value.getExpiresAt().toEpochMilli()));
    }

    @Override
    public AccessToken get() {
        return cache.computeIfAbsent(AccessToken.class, ignored -> {
            final TokenHolder tokenHolder;
            try {
                tokenHolder = authAPI.requestToken(auth0Audience).execute();
            } catch (Auth0Exception e) {
                return null;
            }
            return converter.convert(tokenHolder);
        });
    }
}
