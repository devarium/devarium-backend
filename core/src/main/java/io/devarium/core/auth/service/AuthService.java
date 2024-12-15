package io.devarium.core.auth.service;

import java.util.Map;

public interface AuthService {

    void login(Map<String, Object> userInfo);
}
