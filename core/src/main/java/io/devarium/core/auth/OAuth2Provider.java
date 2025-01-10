package io.devarium.core.auth;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    GITHUB("PROVIDER_GITHUB"),
    GOOGLE("PROVIDER_GOOGLE");

    private final String provider;

    //registrationId로 OAuth2Provider를 찾는 메서드
    public static OAuth2Provider fromString(String registrationId) {
        // 1. name()으로 빠르게 매핑 시도
        try {
            return OAuth2Provider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            // 2. provider 필드와 매핑
            return java.util.Arrays.stream(values())
                .filter(provider -> provider.getProvider().equalsIgnoreCase(registrationId))
                .findFirst()
                .orElseThrow(
                    () -> new CustomAuthException(AuthErrorCode.UNKNOWN_PROVIDER, registrationId));
        }
    }
}
