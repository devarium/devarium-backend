package io.devarium.core.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    GITHUB("PROVIDER_GITHUB"),
    GOOGLE("PROVIDER_GOOGLE");

    private final String provider;
}
