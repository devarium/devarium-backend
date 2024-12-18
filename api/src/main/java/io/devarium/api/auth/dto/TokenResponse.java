package io.devarium.api.auth.dto;

import io.devarium.core.auth.Token;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(Token token) {
        return new TokenResponse(token.accessToken(), token.refreshToken());
    }
}
