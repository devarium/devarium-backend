package io.devarium.core.domain.user;

public record OAuth2UserInfo(
    String id,
    String email,
    String name,
    String picture,
    OAuth2Provider provider
) {

    public static OAuth2UserInfo of(
        String id,
        String email,
        String name,
        String picture,
        OAuth2Provider provider
    ) {
        return new OAuth2UserInfo(id, email, name, picture, provider);
    }
}
