package io.devarium.core.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    GITHUB("PROVIDER_GITHUB"),
    GOOGLE("PROVIDER_GOOGLE");

    private final String provider;
}
