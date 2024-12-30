package io.devarium.core.domain.user.port;

import io.devarium.core.auth.OAuth2UserInfo;

public interface OAuth2Client {

    OAuth2UserInfo getUserInfo(String code);
}
