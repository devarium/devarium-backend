package io.devarium.api.controller.user;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.user.dto.UpdateUserRequest;
import io.devarium.api.controller.user.dto.UserResponse;
import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> getMe(
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        User user = userService.getUser(emailPrincipal.getId());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> updateMe(
        @Valid @RequestBody UpdateUserRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        User user = userService.updateUserProfile(request, emailPrincipal.getUser());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        userService.withdraw(emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
