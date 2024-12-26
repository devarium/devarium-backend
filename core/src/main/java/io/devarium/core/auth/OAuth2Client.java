package io.devarium.core.auth;

public interface OAuth2Client {

    OAuth2UserInfo getUserInfo(String code);
}
