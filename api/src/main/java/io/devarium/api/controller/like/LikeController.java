package io.devarium.api.controller.like;

import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RequestMapping("/api/${api.version}/likes")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{type}/{typeId}")
    public ResponseEntity<Void> like(@PathVariable String type, @PathVariable Long typeId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal) {

        likeService.like(type, typeId, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{type}/{typeId}")
    public ResponseEntity<Void> unlike(@PathVariable String type, @PathVariable Long typeId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal) {

        likeService.unlike(type, typeId, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
