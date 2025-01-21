package io.devarium.api.controller.reply;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.reply.dto.ReplyResponse;
import io.devarium.api.controller.reply.dto.UpsertReplyRequest;
import io.devarium.core.domain.like.service.LikeService;
import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<ReplyResponse>> createReply(
        @Valid @RequestBody UpsertReplyRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Reply reply = replyService.createReply(request, principal.getUser());
        ReplyResponse response = createReplyResponse(reply, principal);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<SingleItemResponse<ReplyResponse>> getReply(
        @PathVariable Long replyId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Reply reply = replyService.getReply(replyId);
        ReplyResponse response = createReplyResponse(reply, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<SingleItemResponse<ReplyResponse>> updateReply(
        @PathVariable Long replyId,
        @Valid @RequestBody UpsertReplyRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Reply reply = replyService.updateReply(replyId, request, principal.getUser());
        ReplyResponse response = createReplyResponse(reply, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
        @PathVariable Long replyId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        replyService.deleteReply(replyId, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{replyId}/likes")
    public ResponseEntity<Void> likeReply(
        @PathVariable Long replyId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Reply reply = replyService.getReply(replyId);
        likeService.like(reply, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{replyId}/likes")
    public ResponseEntity<Void> unlikeReply(
        @PathVariable Long replyId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Reply reply = replyService.getReply(replyId);
        likeService.unlike(reply, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    private ReplyResponse createReplyResponse(Reply reply, CustomUserPrincipal principal) {
        Long likeCount = likeService.getLikeCount(reply);
        Boolean userLiked =
            (principal != null) ? likeService.hasUserLiked(reply, principal.getUser()) : null;
        return ReplyResponse.from(reply, likeCount, userLiked);
    }
}
