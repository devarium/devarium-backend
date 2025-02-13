package io.devarium.core.auth;

public record OAuth2UserInfo(
    String id,
    String email,
    String name,
    String profileImageUrl,
    OAuth2Provider provider
) {

    public static OAuth2UserInfo of(
        String id,
        String email,
        String name,
        String profileImageUrl,
        OAuth2Provider provider
    ) {
        return new OAuth2UserInfo(id, email, name, profileImageUrl, provider);
    }
}
