package io.devarium.api.auth;

import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    //DefaultOAuth2UserService에서 제공하는 정보를 활용해 사용자 정보를 처리하고, 신규 사용자 등록 또는 기존 사용자 조회를 수행
    //기존 사용자의 Provider가 일치하지 않으면 예외를 발생시켜 이메일 중복 사용을 방지

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final UserRepository userRepository;

    // DefaultOAuth2UserService와 UserRepository를 주입받음 -> DefaultOAuth2UserService가 있어야 OAuth2User객체를 가져올수 있다.
    public CustomOAuth2UserService(DefaultOAuth2UserService defaultOAuth2UserService,
        UserRepository userRepository) {
        this.defaultOAuth2UserService = defaultOAuth2UserService;
        this.userRepository = userRepository;
    }

    @Override //DefaultOAuth2UserService를 사용해 OAuth2 인증 후 OAuth2 사용자 정보 가져오기
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // DefaultOAuth2UserService를 활용해 OAuth2 사용자 정보 가져오기
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        System.out.println("OAuth2User attributes: " + oAuth2User.getAttributes());

        // OAuth2 사용자 정보에서 이메일 가져오기
        String email = oAuth2User.getAttribute("email");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Provider provider;
        // OAuth2Provider provider = OAuth2Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        try {
            provider = OAuth2Provider.fromString(registrationId); // 문자열을 Enum으로 변환
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        // 동일 이메일 확인. 이메일 중복 체크:
        //이메일로 기존 사용자를 조회하고, 등록된 Provider가 현재 로그인한 Provider와 다르면 예외던지기
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!existingUser.getProvider().equals(provider)) {
                throw new OAuth2AuthenticationException(
                    "This email is already registered with a different provider: "
                        + existingUser.getProvider()
                );
            }
        });

        // 사용자 정보 조회 또는 신규 사용자 등록
        User user = userRepository.findByEmail(email)
            .orElseGet(() -> registerNewUser(oAuth2User, provider));

        // CustomUserDetails 객체 생성 및 반환: 최종적으로 CustomUserDetails 객체를 생성하여 반환
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2User oAuth2User, OAuth2Provider provider) {
        User user = User.builder()
            .provider(provider)
            .email(oAuth2User.getAttribute("email"))
            .name(oAuth2User.getAttribute("name"))
            .role(UserRole.USER)//기본적으로 UserRole.USER 역할 부여
            .build();
        return userRepository.save(user);
    }
}