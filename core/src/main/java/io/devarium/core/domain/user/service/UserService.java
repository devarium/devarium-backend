package io.devarium.core.domain.user.service;

import io.devarium.core.auth.dto.OAuthUserDetails;
import io.devarium.core.domain.user.User;

public interface UserService {

    User handleSocialLogin(OAuthUserDetails userDetails);

}
