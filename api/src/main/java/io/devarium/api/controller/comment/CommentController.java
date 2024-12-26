package io.devarium.api.controller.comment;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.comment.dto.CommentResponse;
import io.devarium.api.controller.comment.dto.UpsertCommentRequest;
import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<SingleItemResponse<CommentResponse>> createComment(
        @Valid @RequestBody UpsertCommentRequest request
        // @AuthenticationPrincipal UserDetails userDetails
    ) {
        Comment comment = commentService.createComment(request);
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<SingleItemResponse<CommentResponse>> getComment(
        @PathVariable Long commentId
    ) {
        Comment comment = commentService.getComment(commentId);
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<SingleItemResponse<CommentResponse>> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody UpsertCommentRequest request
        // @AuthenticationPrincipal UserDetails userDetails
    ) {
        Comment comment = commentService.updateComment(commentId, request);
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long commentId
        // @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
