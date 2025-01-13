package io.devarium.api.controller.user;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.user.dto.UpdateUserRequest;
import io.devarium.api.controller.user.dto.UserResponse;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.storage.Image;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> getMe(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        User user = userService.getUser(principal.getId());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/profile")
    public ResponseEntity<SingleItemResponse<UserResponse>> updateProfile(
        @Valid @RequestBody UpdateUserRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        User user = userService.updateUserProfile(request, principal.getUser());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/profile/image")
    public ResponseEntity<SingleItemResponse<UserResponse>> updateProfileImage(
        @RequestPart MultipartFile file,
        @AuthenticationPrincipal CustomUserPrincipal emailPrincipal
    ) {
        User user = userService.updateUserProfileImage(Image.from(file), emailPrincipal.getUser());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        userService.withdraw(principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
