package com.financeactive.gatobatch.core.security;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class AccessToken {

    private final String type;

    private final String value;

    private final Instant expiresAt;

    public AccessToken(String type, String value, Instant expiresAt) {
        this.type = requireNonNull(type);
        this.value = requireNonNull(value);
        this.expiresAt = requireNonNull(expiresAt);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
