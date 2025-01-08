package io.devarium.api.auth;

import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보 가져오기
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        // OAuth2 사용자 정보에서 이메일, 이름, 프로필 사진 등 추출
        String id = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Provider provider = OAuth2Provider.fromString(registrationId);

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 user info");
        }

        OAuth2UserInfo userInfo = new OAuth2UserInfo(id, email, name, picture, provider);

        // 동일 이메일로 등록된 다른 Provider 확인
        User existingUser = userService.getUserByEmail(email);
        if (existingUser != null && !existingUser.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationException(
                "This email is already registered with a different provider: "
                    + existingUser.getProvider()
            );
        }

        // 신규 사용자 등록 (로그인 시에는 업데이트하지 않음)
        User user = (existingUser != null)
            ? existingUser
            : userService.createUser(userInfo);

        // 최종적으로 CustomUserDetails 객체 생성 및 반환
        return new CustomUserDetails(user);
    }
}
