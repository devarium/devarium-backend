package io.devarium.api.controller.comment;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.comment.dto.CommentResponse;
import io.devarium.api.controller.comment.dto.UpsertCommentRequest;
import io.devarium.api.controller.reply.dto.ReplyResponse;
import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.port.in.CommentService;
import io.devarium.core.domain.like.port.in.LikeService;
import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.port.in.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/${api.version}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;
    private final ReplyService replyService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<CommentResponse>> createComment(
        @Valid @RequestBody UpsertCommentRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Comment comment = commentService.createComment(request, principal.getUser());
        CommentResponse response = createCommentResponse(comment, principal);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<SingleItemResponse<CommentResponse>> getComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Comment comment = commentService.getComment(commentId);
        CommentResponse response = createCommentResponse(comment, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    // TODO: 책임분리 및 URL 구조 논의 필요
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<PagedListResponse<ReplyResponse>> getRepliesByCommentId(
        @PathVariable Long commentId,
        @PageableDefault(size = Reply.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Page<Reply> replies = replyService.getRepliesByCommentId(commentId, pageable);
        Page<ReplyResponse> replyResponses = replies.map(reply -> {
            Long likeCount = likeService.getLikeCount(reply);
            boolean userLiked =
                principal != null && likeService.hasUserLiked(reply, principal.getUser());
            return ReplyResponse.of(reply, likeCount, userLiked);
        });

        return ResponseEntity.ok(PagedListResponse.from(replyResponses));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<SingleItemResponse<CommentResponse>> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody UpsertCommentRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Comment comment = commentService.updateComment(commentId, request,
            principal.getUser());
        CommentResponse response = createCommentResponse(comment, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        commentService.deleteComment(commentId, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<Void> likeComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Comment comment = commentService.getComment(commentId);
        likeService.like(comment, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<Void> unlikeComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Comment comment = commentService.getComment(commentId);
        likeService.unlike(comment, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    private CommentResponse createCommentResponse(Comment comment, CustomUserPrincipal principal) {
        Long likeCount = likeService.getLikeCount(comment);
        boolean userLiked =
            principal != null && likeService.hasUserLiked(comment, principal.getUser());
        return CommentResponse.of(comment, likeCount, userLiked);
    }
}
